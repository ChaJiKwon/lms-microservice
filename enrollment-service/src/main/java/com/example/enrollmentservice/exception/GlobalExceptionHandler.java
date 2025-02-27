package com.example.enrollmentservice.exception;


import com.example.enrollmentservice.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({CourseNotFoundException.class
            , InvalidCoursePasswordException.class
            , DuplicateEnrollmentException.class
            , StudentNotFoundException.class,})
    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException e) {
        ErrorResponse response = new ErrorResponse();
        response.setException(e.getClass().getSimpleName());
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
