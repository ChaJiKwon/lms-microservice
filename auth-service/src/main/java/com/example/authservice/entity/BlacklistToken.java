package com.example.authservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;


@Entity
@Table(name = "blacklist_tokens")
@Getter
@Setter
public class BlacklistToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blacklist_token",nullable = false, unique = true)
    private String blackListToken;

    @Column(name = "expire_at",nullable = false)
    private Timestamp expireAt;

}
