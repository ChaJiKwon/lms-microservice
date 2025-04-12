package com.example.courseservice.filters;

import com.example.courseservice.exception.LoginRequiredException;
import com.example.courseservice.service.client.UserServiceClient;
import com.example.courseservice.service.util.EmailAuthenticationToken;
import com.example.courseservice.service.util.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserServiceClient userServiceClient;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request
            , @NonNull HttpServletResponse response
            , @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("jwt validation filter....");
        String clientIp = request.getRemoteAddr();
        log.info("Ip:{}", clientIp);
        String uri = request.getRequestURI();
        log.info("Uri: {}", uri);
        // rq from other services, or user testing using swagger:
        if (isInternalRequest(clientIp) || uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs") || uri.startsWith("/actuator")) {
            log.info("This is internal request from services");
            filterChain.doFilter(request, response);
        }
        // else request is not internal
        else {
            String header = request.getHeader("Authorization");
            log.info("not internal");
            if (header == null) {
                throw new LoginRequiredException("You need to login");
            }
            String token = header.substring(7); // remove Bearer
//            boolean isLogout = userServiceClient.isTokenBlacklisted(token);
//            log.info("Is logout: {}", isLogout );
            if (jwtUtils.isTokenExpired(token) || userServiceClient.isTokenBlacklisted(token)) {
                throw new LoginRequiredException("You need to login");
            }
            Claims claims = jwtUtils.getAllClaimsFromToken(token);
            String userEmail = claims.get("email", String.class);
            log.info("User emails: {}", userEmail);
            String roles = claims.get("roles", String.class);
            if (userEmail != null) {
                Authentication auth = new EmailAuthenticationToken(userEmail);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        }
    }

    public boolean isInternalRequest(String ipAddress) {
        return ipAddress.equals("127.0.0.1") || ipAddress.equals("localhost") || ipAddress.startsWith("192.168.") || ipAddress.startsWith("10.");
    }
}
