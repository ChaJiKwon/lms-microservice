package com.example.courseservice.exception;

public class NotATeacherException extends CourseServiceException {
    public NotATeacherException(String message) {
        super(message);
    }
}
