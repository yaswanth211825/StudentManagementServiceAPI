package com.example.API.StudentManagementService.servise.impl;

import com.example.API.StudentManagementService.DTO.CreateStudentDto;
import com.example.API.StudentManagementService.DTO.StudentDto;
import com.example.API.StudentManagementService.Entity.Student;
import com.example.API.StudentManagementService.repository.StudentRepository;
import com.example.API.StudentManagementService.servise.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImplementation implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentDto> getStudentsList() {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> data = students
                .stream()
                .map(student -> new StudentDto(student.getId(),student.getEmail(),student.getName())).toList();
                return data;
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student =  studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no student with that id"));
        StudentDto record = modelMapper.map(student, StudentDto.class);
        return record;

    }
    @Override
    public StudentDto createStudent(CreateStudentDto createStudentDto) {
        Student newStudent = modelMapper.map(createStudentDto,Student.class);
        Student student = studentRepository.save(newStudent);
        return modelMapper.map(student,StudentDto.class);

    }

    @Override
    public void deleteStudentById(Long id) {
        if(!studentRepository.existsById(id)){
            throw new IllegalArgumentException("no student with that" + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public StudentDto updateStudent(Long id, CreateStudentDto createStudentDto) {
//        if(!studentRepository.existsById(id)){
//            throw new IllegalArgumentException("no student with that " + id);
//        }
        Student updateStudent = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no student with that id " + id));
        modelMapper.map(createStudentDto,updateStudent);
        studentRepository.save(updateStudent);
        return modelMapper.map(updateStudent,StudentDto.class);
    }

    @Override
    public StudentDto modifyStudent(Long id, Map<String , Object> updates) {
        Student updateStudent = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no student with that id " + id));
        updates.forEach((field, value) -> {
                    switch (field) {
                        case "name" : updateStudent.setName((String) value);
                        break;
                        case "email" : updateStudent.setEmail((String) value);
                        break;
                        default : throw new IllegalArgumentException("Invalid field: " + field);

                    }
                }
                );
        studentRepository.save(updateStudent);
        return modelMapper.map(updateStudent , StudentDto.class);

        }
}
