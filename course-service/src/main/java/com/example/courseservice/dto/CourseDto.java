package com.example.courseservice.dto;
import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdBy;
    private Timestamp createdAt;
    private String updatedBy;
    private Timestamp updatedAt;
}
