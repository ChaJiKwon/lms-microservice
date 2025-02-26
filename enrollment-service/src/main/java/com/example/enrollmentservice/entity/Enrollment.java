package com.example.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "enrollments")
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_email",nullable = false)
    private String studentEmail;
    @Column(name = "course_name",nullable = false)
    private String courseName;
    @Column(nullable = false)
    private boolean isRegistered=false;
}
