package com.example.apigateway.exception;

public class ForbiddenUrlException extends RuntimeException {
    public ForbiddenUrlException(String message){
        super(message);
    }
}
