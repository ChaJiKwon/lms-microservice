package com.example.authservice.dto.responses;
import lombok.Data;


@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String expirationTime;
}

