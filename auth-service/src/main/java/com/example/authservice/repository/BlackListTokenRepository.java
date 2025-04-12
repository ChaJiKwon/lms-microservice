package com.example.authservice.repository;

import com.example.authservice.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<BlacklistToken,Long> {
    boolean existsByBlackListToken(String token);
}
