package com.example.API.StudentManagementService;

import com.example.API.StudentManagementService.repository.StudentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentLifecycleIntegrationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private StudentRepository studentRepository;

    @BeforeEach
    void clearRecords() {
        studentRepository.deleteAll();
    }

    @Test
    void completeLifecyclePersistsTheTargetedUpdateAndPreservesTheOtherRecord() throws Exception {
        long recordA = createStudent("QA Lifecycle A", "qa.a@example.test");
        long recordB = createStudent("QA Lifecycle B", "qa.b@example.test");

        mockMvc.perform(put("/students/{id}", recordA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"QA Lifecycle A Updated\",\"email\":\"qa.a.updated@example.test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recordA))
                .andExpect(jsonPath("$.name").value("QA Lifecycle A Updated"));

        Map<Long, JsonNode> reloadedRecords = recordsById();
        assertThat(reloadedRecords).hasSize(2);
        assertStudent(reloadedRecords.get(recordA), recordA, "QA Lifecycle A Updated", "qa.a.updated@example.test");
        assertStudent(reloadedRecords.get(recordB), recordB, "QA Lifecycle B", "qa.b@example.test");

        mockMvc.perform(delete("/students/{id}", recordA)).andExpect(status().isNoContent());

        Map<Long, JsonNode> afterDeletion = recordsById();
        assertThat(afterDeletion).containsOnlyKeys(recordB);
        assertStudent(afterDeletion.get(recordB), recordB, "QA Lifecycle B", "qa.b@example.test");
    }

    @Test
    void patchChangesOnlyTheRequestedFieldOnTheRequestedRecord() throws Exception {
        long recordA = createStudent("QA Patch A", "qa.patch.a@example.test");
        long recordB = createStudent("QA Patch B", "qa.patch.b@example.test");

        mockMvc.perform(patch("/students/{id}", recordA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"QA Patch A Renamed\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("QA Patch A Renamed"))
                .andExpect(jsonPath("$.email").value("qa.patch.a@example.test"));

        Map<Long, JsonNode> reloadedRecords = recordsById();
        assertStudent(reloadedRecords.get(recordA), recordA, "QA Patch A Renamed", "qa.patch.a@example.test");
        assertStudent(reloadedRecords.get(recordB), recordB, "QA Patch B", "qa.patch.b@example.test");
    }

    @Test
    void rejectsInvalidCreateAndUpdateRequests() throws Exception {
        long record = createStudent("QA Valid", "qa.valid@example.test");
        String invalidPayload = "{\"name\":\"X\",\"email\":\"not-an-email\"}";

        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(invalidPayload))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/students/{id}", record).contentType(MediaType.APPLICATION_JSON).content(invalidPayload))
                .andExpect(status().isBadRequest());

        assertStudent(recordsById().get(record), record, "QA Valid", "qa.valid@example.test");
    }

    @Test
    void rejectsDuplicateStudentEmailAddresses() throws Exception {
        createStudent("QA Duplicate A", "qa.duplicate@example.test");

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"QA Duplicate B\",\"email\":\"qa.duplicate@example.test\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void returnsNotFoundForMissingRecords() throws Exception {
        mockMvc.perform(get("/students/{id}", 999999L)).andExpect(status().isNotFound());
        mockMvc.perform(put("/students/{id}", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"QA Missing\",\"email\":\"qa.missing@example.test\"}"))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/students/{id}", 999999L)).andExpect(status().isNotFound());
    }

    @Test
    void servesTheBrowserTestPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("index.html"));
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Student records")));
    }

    private long createStudent(String name, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", name, "email", email))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private Map<Long, JsonNode> recordsById() throws Exception {
        MvcResult result = mockMvc.perform(get("/students")).andExpect(status().isOk()).andReturn();
        Map<Long, JsonNode> records = new HashMap<>();
        for (JsonNode student : objectMapper.readTree(result.getResponse().getContentAsString())) {
            records.put(student.get("id").asLong(), student);
        }
        return records;
    }

    private void assertStudent(JsonNode actual, long id, String name, String email) {
        assertThat(actual).isNotNull();
        assertThat(actual.get("id").asLong()).isEqualTo(id);
        assertThat(actual.get("name").asText()).isEqualTo(name);
        assertThat(actual.get("email").asText()).isEqualTo(email);
    }
}
