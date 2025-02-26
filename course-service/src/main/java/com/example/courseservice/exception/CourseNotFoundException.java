package com.example.courseservice.exception;

public class CourseNotFoundException extends CourseServiceException{
    public CourseNotFoundException(String message){
        super(message);
    }
}
