
package com.example.apigateway.response;
import lombok.Data;
@Data
public class ErrorResponse {
    private String path;
    private String exception;
    private String message;
    private int statusCode;
}



