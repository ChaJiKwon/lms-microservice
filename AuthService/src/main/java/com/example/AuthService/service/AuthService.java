package com.example.AuthService.service;

import com.example.AuthService.Client.UserService;

import com.example.AuthService.dto.LoginDto;
import com.example.AuthService.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService  {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<String> register(UserDto userDTO){
        UserDto newUser = new UserDto();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//      System.out.println("Raw password before encode: " + userDTO.getPassword());
//      System.out.println("Encoded password: " + newUser.getPassword());
        userService.registerNewUser(newUser);
        return new ResponseEntity<>("Success register", HttpStatus.OK);
    }


    public ResponseEntity<String> login(LoginDto loginDto) {
        try {
            System.out.println("Login info: "+ loginDto.getEmail()+ loginDto.getPassword());
            UsernamePasswordAuthenticationToken upat=  new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            System.out.println("Token: " + upat);
            Authentication auth = authenticationManager.authenticate(upat);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<>("Login success", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
