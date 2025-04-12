package com.example.courseservice.exception;

public class LoginRequiredException extends RuntimeException{
    public LoginRequiredException(String message){
        super(message);
    }
}