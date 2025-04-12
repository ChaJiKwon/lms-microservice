package com.example.authservice.service;
import com.example.authservice.entity.BlacklistToken;
import com.example.authservice.exception.LoginErrorException;
import com.example.authservice.repository.BlackListTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BlacklistTokenService {
    private final BlackListTokenRepository blackListTokenRepository;
    private final JWTService jwtService;

    public void save(String token){
        BlacklistToken blacklistToken = new BlacklistToken();
        blacklistToken.setBlackListToken(token);
        Timestamp expireTime =  new Timestamp(jwtService.extractExpiration(token).getTime()) ;
        blacklistToken.setExpireAt(expireTime);
        blackListTokenRepository.save(blacklistToken);
    }

    public boolean isTokenBlacklisted(String token){
        return blackListTokenRepository.existsByBlackListToken(token);
    }

}
