package com.example.enrollmentservice.exception;

public class DuplicateEnrollmentException extends RuntimeException{
    public DuplicateEnrollmentException(String message){
        super(message);
    }
}
