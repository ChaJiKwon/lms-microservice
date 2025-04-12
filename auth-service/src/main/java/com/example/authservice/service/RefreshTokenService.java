package com.example.authservice.service;

import com.example.authservice.dto.TokenRefreshDto;
import com.example.authservice.dto.responses.RefreshTokenResponse;
import com.example.authservice.entity.RefreshToken;
import com.example.authservice.exception.LoginErrorException;
import com.example.authservice.exception.TokenNotExistException;
import com.example.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder encoder;


    public String createRefreshToken(String userEmail){
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserEmail(userEmail);
        RefreshToken refreshToken = new RefreshToken();
        String token = UUID.randomUUID().toString();
        if (existingToken.isPresent()){
            refreshToken = existingToken.get();
            Timestamp expiryDate = new Timestamp(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
            refreshToken.setExpiredAt(expiryDate);
            refreshToken.setToken(encoder.encode(token));
        }
        // 7 days
        Timestamp expiryDate = new Timestamp(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        refreshToken.setExpiredAt(expiryDate);
        refreshToken.setToken(encoder.encode(token));
        refreshToken.setExpiredAt(expiryDate);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public Optional<RefreshToken> verifyToken(String refreshToken){
        return refreshTokenRepository.findAll().stream()
                .filter(rt -> encoder.matches(refreshToken, rt.getToken()))
                .filter(rt -> rt.getExpiredAt().after(new Timestamp(System.currentTimeMillis())))
                .findFirst();
    }

    public void deleteRefreshToken(String userEmail){
        refreshTokenRepository.deleteByUserEmail(userEmail)
                .orElseThrow(() -> new TokenNotExistException("Refresh token not exist"));
    }

}
