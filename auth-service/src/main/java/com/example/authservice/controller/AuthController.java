package com.example.authservice.controller;
import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.TokenRefreshDto;
import com.example.authservice.dto.UserDto;
import com.example.authservice.dto.responses.LoginResponse;
import com.example.authservice.dto.responses.RefreshTokenResponse;
import com.example.authservice.exception.LoginErrorException;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.BlacklistTokenService;
import com.example.authservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    final AuthService authService;
    final RefreshTokenService refreshTokenService;
    final BlacklistTokenService blacklistTokenService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        System.out.println("Registering new user...");
        return authService.register(userDto);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshAccessToken(@RequestBody TokenRefreshDto request){
        return authService.refreshAccessToken(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String response = authService.logout(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/check-blacklist-token")
    public Boolean isTokenBlacklist(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header==null )
            return false;
        String token = header.substring(7);
        return blacklistTokenService.isTokenBlacklisted(token);
    }

}
