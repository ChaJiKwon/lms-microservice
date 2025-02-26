package com.example.authservice.security;

import com.example.authservice.service.UserService;
import com.example.authservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // extract user input credential
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        // get user email from db and password for custom authenticate
        UserDto user = userService.findByEmail(email);
//      System.out.println("db encoded password " + user.getPassword());
        if (Boolean.TRUE.equals(userService.existsByEmail(email))){
            if (passwordEncoder.matches(password, user.getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (!user.getRoles().isEmpty()) {
                    String role = user.getRoles().get(0).getRoleName();
                    log.info("Roleeeeee: " + role);
                    authorities.add(new SimpleGrantedAuthority(role));
                } else {
                    log.warn("User does not have any assigned roles!");
                }

                return new UsernamePasswordAuthenticationToken(email, password, authorities);
            } else {
                log.error("Invalid email or Password");
                throw new BadCredentialsException("Invalid email or Password");
            }

        }
        log.error("Invalid email or Password");
        throw new BadCredentialsException("Invalid email or Password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
