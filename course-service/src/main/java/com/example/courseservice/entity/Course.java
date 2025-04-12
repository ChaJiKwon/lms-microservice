package com.example.courseservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "created_by",updatable = false)
    @CreatedBy
    private String createdBy;
    @CreatedDate
    @Column(name = "created_at",updatable = false)
    private Timestamp createdAt;

    @Column(name = "update_by")
    @LastModifiedBy
    private String updateBy;
    @LastModifiedDate
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "is_active",  nullable = false)
    private boolean isActive=true;
    @Column(name = "is_delete",nullable = false)
    private boolean isDelete=false;

}
