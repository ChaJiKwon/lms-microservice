package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshDto {
    private String refreshToken;
}
