package com.example.courseservice.dto.response;

import lombok.Data;

@Data
public class CustomResponse {
    private int statusCode;
    private String message;
}
