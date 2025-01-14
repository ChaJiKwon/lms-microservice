package com.example.UserService.Service;


import com.example.UserService.Entity.User;
import com.example.UserService.Repository.UserRepository;
import com.example.UserService.Security.UsernamePasswordAuthenticationProvider;
import com.example.UserService.dto.LoginDto;
import com.example.UserService.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserService userservice;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsernamePasswordAuthenticationProvider provider;
    @Autowired
    CustomUserDetailService userDetailService;
    @Autowired
    JWTService jwtService;
    public ResponseEntity<String> register(User user){
        if (Boolean.TRUE.equals(userservice.existsByEmail(user.getEmail()))){
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userservice.save(user);
        return new ResponseEntity<>("Register success", HttpStatus.OK);
    }
    public ResponseEntity<LoginResponse> login(LoginDto loginDto) {
        try {
            Authentication authentication = provider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("authentication obj: " + authentication.isAuthenticated());
            String jwt ="";
            if (authentication.isAuthenticated()){
                jwt = jwtService.generateToken(authentication);
                System.out.println( "jwt: "+jwt);
            }
            System.out.println("Username from token: " +jwtService.getEmailFromToken(jwt));
            UserDetails ud = userDetailService.loadUserByUsername(loginDto.getEmail());
            System.out.println("Is token valid: " + jwtService.isTokenValid(jwt,ud));
            LoginResponse response = new LoginResponse();
            response.setToken(jwt);
            response.setExpirationTime(jwtService.extractExpiration(jwt).toString());
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception ex) {
            LoginResponse response = new LoginResponse();
            response.setToken("Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
