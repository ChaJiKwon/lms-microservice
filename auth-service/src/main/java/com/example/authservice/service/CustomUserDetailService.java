package com.example.authservice.service;
import com.example.authservice.dto.AuthDto;
import com.example.authservice.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String userServiceUrl = "http://user-service/user/" + email;
        System.out.println("username: " + email);
        AuthDto authDto=  restTemplate.getForObject(userServiceUrl, AuthDto.class);
        List<GrantedAuthority> authorities = new ArrayList<>();

        assert authDto != null;
        if (!authDto.getRoles().isEmpty()) {
            String role = authDto.getRoles().get(0).getRoleName();
            log.info("Roleeeeee: " + role);
            authorities.add(new SimpleGrantedAuthority(role));
        } else {
            log.warn("User does not have any assigned roles!");
        }
        return new User(authDto.getEmail(),authDto.getPassword(),authorities);
    }
}
