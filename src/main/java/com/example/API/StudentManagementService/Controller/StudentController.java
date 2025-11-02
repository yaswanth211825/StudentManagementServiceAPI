package com.example.API.StudentManagementService.Controller;
import java.util.*;

import com.example.API.StudentManagementService.DTO.CreateStudentDto;
import com.example.API.StudentManagementService.DTO.StudentDto;
import com.example.API.StudentManagementService.servise.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getStudent(){
        return ResponseEntity.status(OK).body(studentService.getStudentsList());
    }
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id){
        return ResponseEntity.status(OK).body(studentService.getStudentById(id));
    }
    @PostMapping("/students")
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid CreateStudentDto addStudentRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(addStudentRequestDto));

    }
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id,@RequestBody CreateStudentDto createStudentDto){
            // Implementation for updating a student would go here
           return ResponseEntity.status(OK).body(studentService.updateStudent(id, createStudentDto));
    }
    @PatchMapping("/students/{id}")
    public ResponseEntity<StudentDto> modifyStudent(@PathVariable Long id,@RequestBody Map<String , Object> updates){
        return ResponseEntity.status(OK).body(studentService.modifyStudent(id,updates));
    }
    }

