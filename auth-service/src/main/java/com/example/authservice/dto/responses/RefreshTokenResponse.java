package com.example.authservice.dto.responses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {
    private String newAccessToken;
    private String refreshToken;
    private String expiredTime;
}
