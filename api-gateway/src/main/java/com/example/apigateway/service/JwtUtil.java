package com.example.apigateway.service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtKey;
    public SecretKey getStringToKey(String key){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public Claims getAllClaimsFromToken(String token) {
        SecretKey secretKey =  getStringToKey(jwtKey);
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
