package com.example.authservice.exception.responses;

import lombok.Data;

@Data
public class LoginErrorResponse {
    private String message;
    private int statusCode;
}

