package com.example.authservice.exception;

public class TokenNotExistException  extends RuntimeException{
    public TokenNotExistException(String message){
        super(message);
    }
}
