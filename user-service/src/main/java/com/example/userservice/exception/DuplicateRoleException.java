package com.example.userservice.exception;

public class DuplicateRoleException extends RuntimeException{
    public DuplicateRoleException(String message){
        super(message);
    }
}
