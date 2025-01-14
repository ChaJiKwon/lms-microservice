package com.example.UserService.Security;
import com.example.UserService.Entity.User;
import com.example.UserService.Service.UserService;
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
        System.out.println(" Raw password: " + password);
        // get user email from db and password for custom authenticate
        User user = userService.findByEmail(email);
//      System.out.println("db encoded password " + user.getPassword());
        if (Boolean.TRUE.equals(userService.existsByEmail(email))){
            if (passwordEncoder.matches(password, user.getPassword())){
                List<GrantedAuthority> authorities= new ArrayList<>();
//                String role = user.getRoles().get(0).getRoleName();
                authorities.add(new SimpleGrantedAuthority(user.getRoles().get(0).getRoleName()));

                return new UsernamePasswordAuthenticationToken(email,password,authorities);
            }
            else {
                throw new BadCredentialsException("Invalid email or Password");
            }
        }
        throw new BadCredentialsException("Invalid email or Password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
