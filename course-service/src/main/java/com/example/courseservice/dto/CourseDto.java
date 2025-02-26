package com.example.courseservice.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
