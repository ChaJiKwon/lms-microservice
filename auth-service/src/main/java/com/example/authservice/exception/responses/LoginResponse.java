package com.example.authservice.exception.responses;
import lombok.Data;


@Data
public class LoginResponse {
    private String token;
    private String expirationTime;
}

