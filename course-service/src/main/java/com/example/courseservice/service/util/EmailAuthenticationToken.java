package com.example.courseservice.service.util;



import org.springframework.security.authentication.AbstractAuthenticationToken;


public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    public EmailAuthenticationToken(String email) {
        super(null);
        this.email = email;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}
