package com.example.UserService.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
public class CustomSuccessAuthHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Optional<String> roles = (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst());
        if (roles.orElse("").equalsIgnoreCase("student")){
            response.sendRedirect("/student");
        } else if (roles.orElse("").equalsIgnoreCase("teacher")) {
            response.sendRedirect("/user");
        }
    }
}
