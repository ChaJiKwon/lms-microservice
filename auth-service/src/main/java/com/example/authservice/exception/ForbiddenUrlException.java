package com.example.authservice.exception;

public class ForbiddenUrlException extends RuntimeException{
    public ForbiddenUrlException(String message){
        super(message);
    }
}
