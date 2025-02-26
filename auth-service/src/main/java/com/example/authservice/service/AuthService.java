package com.example.authservice.service;


import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.UserDto;
import com.example.authservice.exception.LoginErrorException;
import com.example.authservice.exception.responses.LoginResponse;
import com.example.authservice.security.UsernamePasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService  {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UsernamePasswordAuthenticationProvider provider;
    private final JWTService jwtService;
    private final CustomUserDetailService userDetailService;

    public ResponseEntity<String> register(UserDto userDTO){
        if (Boolean.TRUE.equals(userService.existsByEmail(userDTO.getEmail()))){
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        UserDto newUser = new UserDto();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//      System.out.println("Raw password before encode: " + userDTO.getPassword());
//      System.out.println("Encoded password: " + newUser.getPassword());
        userService.saveUser(newUser);
        return new ResponseEntity<>("Success register", HttpStatus.OK);
    }

    public ResponseEntity<LoginResponse> login(LoginDto loginDto) {
        try {
            Authentication authentication = provider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("authentication obj: " + authentication.isAuthenticated());
            String jwt ="";
            if (authentication.isAuthenticated()){
                jwt = jwtService.generateToken(authentication);
                System.out.println( "jwt: "+jwt);
            }
            LoginResponse response = new LoginResponse();
            response.setToken(jwt);
            response.setExpirationTime(jwtService.extractExpiration(jwt).toString());
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception ex) {
            throw new LoginErrorException("Invalid email or password ! Please try again");
        }
    }
}
