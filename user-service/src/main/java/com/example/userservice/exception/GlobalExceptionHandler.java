package com.example.userservice.exception;

import com.example.userservice.dto.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EmailNotFoundException.class, IDNotFoundException.class,
            InvalidRoleException.class, DuplicateRoleException.class, DeletedUserException.class})
    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
