package com.example.authservice.exception;

import com.example.authservice.exception.responses.LoginErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginErrorException.class)
    public ResponseEntity<Object> handleLoginError(LoginErrorException e){
        LoginErrorResponse response = new LoginErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
}
