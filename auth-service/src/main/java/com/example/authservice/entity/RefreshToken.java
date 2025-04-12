package com.example.authservice.entity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.sql.Timestamp;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(name = "user_email",nullable = false)
    @CreatedBy
    private String userEmail;
    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

}
