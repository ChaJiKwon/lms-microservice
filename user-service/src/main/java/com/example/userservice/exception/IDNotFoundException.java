package com.example.userservice.exception;

public class IDNotFoundException extends RuntimeException{
    public IDNotFoundException(String message){
        super(message);
    }
}
