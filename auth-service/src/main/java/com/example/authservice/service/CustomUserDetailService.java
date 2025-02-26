package com.example.authservice.service;
import com.example.authservice.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String userServiceUrl = "http://user-service//" + email;
        System.out.println("username: " + email);
        LoginDto loginDto=  restTemplate.getForObject(userServiceUrl, LoginDto.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        assert loginDto != null;
        return new User(loginDto.getEmail(),loginDto.getPassword(),authorities);
    }
}
