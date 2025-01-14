package com.example.UserService.Controller;
import com.example.UserService.Entity.User;
import com.example.UserService.Service.AuthService;
import com.example.UserService.Service.UserService;
import com.example.UserService.dto.LoginDto;
import com.example.UserService.responses.LoginResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        return authService.register(user);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }
}
