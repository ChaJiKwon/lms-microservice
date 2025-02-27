package com.example.enrollmentservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> teachers;
}
