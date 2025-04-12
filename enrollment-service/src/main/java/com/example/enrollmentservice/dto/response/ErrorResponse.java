package com.example.enrollmentservice.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int statusCode;
}
