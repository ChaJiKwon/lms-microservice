package com.example.userservice.dto;

import lombok.Data;

import java.util.List;

/**
 * this dto send user detail to auth-service for authen
 */
@Data
public class AuthDto {
    private String email;
    private String password;
    List<RoleDto> roles;
}
