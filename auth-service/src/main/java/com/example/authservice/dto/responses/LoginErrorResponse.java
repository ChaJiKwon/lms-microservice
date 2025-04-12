package com.example.authservice.dto.responses;

import lombok.Data;

@Data
public class LoginErrorResponse {
    private String message;
    private int statusCode;
}

