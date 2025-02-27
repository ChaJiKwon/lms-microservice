package com.example.enrollmentservice.exception;

public class InvalidCoursePasswordException extends RuntimeException{
    public InvalidCoursePasswordException(String message){
        super(message);
    }
}
