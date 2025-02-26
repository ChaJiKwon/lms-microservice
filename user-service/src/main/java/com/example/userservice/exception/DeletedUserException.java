package com.example.userservice.exception;

public class DeletedUserException extends RuntimeException{
    public DeletedUserException(String message){
        super(message);
    }
}
