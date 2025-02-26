package com.example.userservice.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String method;
    @Column(name = "role_name")
    private String roleName;
}
