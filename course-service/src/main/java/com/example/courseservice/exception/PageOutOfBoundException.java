package com.example.courseservice.exception;

public class PageOutOfBoundException extends CourseServiceException{
    public PageOutOfBoundException(String message){
        super(message);
    }
}
