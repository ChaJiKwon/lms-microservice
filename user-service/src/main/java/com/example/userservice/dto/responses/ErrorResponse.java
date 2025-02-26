package com.example.userservice.dto.responses;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int statusCode;
}
