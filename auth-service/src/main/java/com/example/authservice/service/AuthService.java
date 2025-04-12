package com.example.authservice.service;


import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.TokenRefreshDto;
import com.example.authservice.dto.UserDto;
import com.example.authservice.dto.responses.RefreshTokenResponse;
import com.example.authservice.exception.LoginErrorException;
import com.example.authservice.dto.responses.LoginResponse;
import com.example.authservice.security.UsernamePasswordAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService  {
    UserService userService;
    PasswordEncoder passwordEncoder;
    UsernamePasswordAuthenticationProvider provider;
    JWTService jwtService;
    RefreshTokenService refreshTokenService;
    BlacklistTokenService blacklistTokenService;
    CustomUserDetailService customUserDetailService;

    public ResponseEntity<String> register(UserDto userDTO){
        if (Boolean.TRUE.equals(userService.existsByEmail(userDTO.getEmail()))){
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        UserDto newUser = new UserDto();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.saveUser(newUser);
        return new ResponseEntity<>("Success register", HttpStatus.OK);
    }

    public ResponseEntity<LoginResponse> login(LoginDto loginDto) {
        try {
            Authentication authentication = provider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            System.out.println("authentication obj: " + authentication.isAuthenticated());
            String jwt ="";
            if (authentication.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authentication);
                jwt = jwtService.generateToken(authentication);
                System.out.println( "jwt: "+jwt);
            }
            LoginResponse response = new LoginResponse();
            response.setAccessToken(jwt);
            String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            response.setRefreshToken(refreshTokenService.createRefreshToken(userEmail));
            response.setExpirationTime(jwtService.extractExpiration(jwt).toString());
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception ex) {
            throw new LoginErrorException("Invalid email or password ! Please try again");
        }
    }


    public RefreshTokenResponse refreshAccessToken(TokenRefreshDto request){
        String refreshToken = request.getRefreshToken();
        return  refreshTokenService.verifyToken(refreshToken)
                .map(token -> {
                    // Tạo access token mới
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(token.getUserEmail());
                    log.info("USER DETAILS {}",userDetails);
                    // Tạo đối tượng Authentication với thông tin đầy đủ
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String newAccessToken = jwtService.generateToken(authentication);
                    RefreshTokenResponse response = new RefreshTokenResponse();
                    response.setNewAccessToken(newAccessToken);
                    response.setRefreshToken(refreshToken);
                    response.setExpiredTime(jwtService.extractExpiration(newAccessToken).toString());
                    return response;
                })
                .orElseThrow(() -> new LoginErrorException("Refresh Token is invalid, please try again"));
    }

    // luu id , not token
    @Transactional
    public String logout(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header==null){
            throw new LoginErrorException("You need to login");
        }

        String token = header.substring(7);
        if (blacklistTokenService.isTokenBlacklisted(token)){
            throw new LoginErrorException("Unauthenticated !");
        }
        boolean tokenExpired = jwtService.isTokenExpired(token);
        log.info("Is expired: {}", tokenExpired);
        if (jwtService.isTokenExpired(token)){
            throw new LoginErrorException("You need to login");
        }
        String userEmail = jwtService.getEmailFromToken(token);
        blacklistTokenService.save(token);
        refreshTokenService.deleteRefreshToken(userEmail);
        return "Logout success";
    }
}
