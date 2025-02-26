package com.example.courseservice.exception;

public class TeacherNotExistException extends CourseServiceException{
    public TeacherNotExistException(String message) {
        super(message);
    }
}
