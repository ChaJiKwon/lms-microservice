package com.example.authservice.controller;
import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.UserDto;
import com.example.authservice.exception.responses.LoginResponse;
import com.example.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        System.out.println("Registering new user...");
        return authService.register(userDto);
    }
}
