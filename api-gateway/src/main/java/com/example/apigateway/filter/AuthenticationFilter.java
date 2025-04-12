package com.example.apigateway.filter;

import com.example.apigateway.exception.LoginRequiredException;
import com.example.apigateway.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationFilter implements GatewayFilter {
    private final JwtUtil jwtUtil;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // Kiểm tra xem request có Authorization header( tuc la user da login ) hay ko
        if (isAuthMissing(request)) {
            log.info("Authorization header is missing");
            throw new  LoginRequiredException("You need to login to access to this page");
        }

        String token = getAuthHeader(request).substring(7); // Bỏ "Bearer "
        log.info("Token received: {}", token);
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String email = claims.get("email", String.class);
        String roles = claims.get("roles", String.class);
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("email", email)
                .header("roles", roles)
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization");
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
