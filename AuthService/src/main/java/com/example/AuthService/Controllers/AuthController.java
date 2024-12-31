package com.example.AuthService.Controllers;
import com.example.AuthService.dto.LoginDto;

import com.example.AuthService.dto.UserDto;
import com.example.AuthService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDto userDTO){
        return authService.register(userDTO);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }
}
