package com.example.API.StudentManagementService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateStudentDto {
    @NotBlank
    @Size(message = "Name must be between 2 and 50 characters", min = 2, max = 50)
    private String name;
    @Email(message = "Email is Required")
    @NotBlank
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
