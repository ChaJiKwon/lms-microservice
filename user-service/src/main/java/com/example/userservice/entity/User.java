package com.example.userservice.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Role> roles;
    @Column(name = "is_delete")
    private boolean isDelete=false;
}
