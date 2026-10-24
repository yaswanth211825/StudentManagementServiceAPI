package com.example.API.StudentManagementService.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("No student with id " + id);
    }
}
