package com.example.courseservice.entity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "course_teacher")
public class CourseTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "course_name", nullable = false)
    private String courseName;
    @Column(name = "teacher_email" ,nullable = false)
    private String teacherEmail;
}
