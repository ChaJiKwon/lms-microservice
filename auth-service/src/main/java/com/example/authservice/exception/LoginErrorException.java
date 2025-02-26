package com.example.authservice.exception;


public class LoginErrorException extends RuntimeException{
    public LoginErrorException(String message){
        super(message);
    }
}
