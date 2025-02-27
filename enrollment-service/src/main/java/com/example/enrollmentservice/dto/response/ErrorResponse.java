package com.example.enrollmentservice.dto.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String exception;
    private String message;
    private int statusCode;
}
