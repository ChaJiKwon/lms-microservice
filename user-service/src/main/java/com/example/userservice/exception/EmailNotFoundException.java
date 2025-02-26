package com.example.userservice.exception;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String message){
        super(message);
    }
}
