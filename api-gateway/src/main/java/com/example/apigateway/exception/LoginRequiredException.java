package com.example.apigateway.exception;

public class LoginRequiredException extends RuntimeException{
    public LoginRequiredException(String message){
        super(message);
    }
}
