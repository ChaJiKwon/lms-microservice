package com.example.authservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthDto {
    private String email;
    private String password;
    List<RoleDto> roles;
}
