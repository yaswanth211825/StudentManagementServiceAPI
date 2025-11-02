package com.example.API.StudentManagementService.practise;

import org.springframework.stereotype.Component;

@Component
public class HelloWorld {
    public String display(){
        String start = "Rest API started";
        System.out.println(start);
        return "hi guys";
    }
}
