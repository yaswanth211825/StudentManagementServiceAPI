package com.example.API.StudentManagementService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStudentDto {
    @NotBlank
    @Size(message = "Name must be between 2 and 50 characters", min = 2, max = 50)
    private String name;
    @Email(message = "Email is Required")
    @NotBlank
    private String email;
}
