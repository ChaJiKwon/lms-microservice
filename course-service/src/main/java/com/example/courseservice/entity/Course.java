package com.example.courseservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "course_name",nullable = false)
    private String courseName;
    @Column(nullable = false)
    private String description;
    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date",nullable = false)
    private LocalDate endDate;
    @Column(name = "is_active",  nullable = false)
    private boolean isActive=true;
    @Column(name = "is_delete",nullable = false)
    private boolean isDelete=false;

}
