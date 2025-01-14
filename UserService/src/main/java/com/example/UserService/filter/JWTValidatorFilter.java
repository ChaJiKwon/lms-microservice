package com.example.UserService.filter;
import com.example.UserService.constant.SecurityConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
/**
 * validate token
 */
public class JWTValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("this is jwt filter");
        String jwt = request.getHeader(SecurityConst.HEADER);
        if (jwt != null ) {
            try {
                jwt = request.getHeader(SecurityConst.HEADER).substring(7);
                SecretKey key = Keys.hmacShaKeyFor(SecurityConst.JWT_KEY.getBytes(StandardCharsets.UTF_8));
                System.out.println( "JWT TOKEN : "+jwt);
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build().parseSignedClaims(jwt).getPayload();
                String username = String.valueOf(claims.get("email"));
                String authorities = claims.get("authorities").toString();
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username,
                null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }
}
