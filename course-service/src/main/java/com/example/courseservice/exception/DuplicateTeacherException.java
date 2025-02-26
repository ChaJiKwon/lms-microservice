package com.example.courseservice.exception;



public class DuplicateTeacherException extends CourseServiceException {
    public DuplicateTeacherException(String message) {
        super(message);
    }
}
