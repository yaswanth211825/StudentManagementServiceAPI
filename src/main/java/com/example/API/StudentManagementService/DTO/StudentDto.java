package com.example.API.StudentManagementService.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    Long id;
    String name;
    String email;
}
