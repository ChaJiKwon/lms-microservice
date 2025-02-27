package com.example.enrollmentservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String email;
    private String username;
    private List<String> enrolledCourse;
}
