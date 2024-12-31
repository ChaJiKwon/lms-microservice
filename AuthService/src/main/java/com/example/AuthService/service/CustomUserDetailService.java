package com.example.AuthService.service;
import com.example.AuthService.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String userServiceUrl = "http://localhost:8080/users/" + email;
        System.out.println("username: " + email);

        LoginDto loginDto=  restTemplate.getForObject(userServiceUrl, LoginDto.class);
        System.out.println(new CustomUserDetail(loginDto).getPassword());
        return new CustomUserDetail(loginDto);
    }
}
