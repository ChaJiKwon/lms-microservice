package com.example.userservice.dto.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String expirationTime;
}
