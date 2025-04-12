package com.example.courseservice.exception;

import com.example.courseservice.dto.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CourseServiceException.class)
    public ResponseEntity<Object> handleCourseException(CourseServiceException e){
        CustomResponse response = new CustomResponse();
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
