package com.example.API.StudentManagementService.servise;

import com.example.API.StudentManagementService.DTO.CreateStudentDto;
import com.example.API.StudentManagementService.DTO.StudentDto;

import java.util.List;
import java.util.Map;

public interface StudentService {
    List<StudentDto> getStudentsList();

    StudentDto getStudentById(Long id);

    StudentDto createStudent(CreateStudentDto createStudentDto);

    void deleteStudentById(Long id);

    StudentDto updateStudent(Long id, CreateStudentDto createStudentDto);

    StudentDto modifyStudent(Long id, Map<String , Object> updates);
}
