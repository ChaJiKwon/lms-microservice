package com.example.courseservice.service.client;

import com.example.courseservice.dto.RoleDto;
import com.example.courseservice.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_DETAIL_URL = "http://user-service/user/email/";
    private static final String USER_SERVICE_EXISTSBYEMAIL_URL = "http://user-service/user/exists/";
    private static final String AUTH_SERVICE_DETAIL_URL = "http://auth-service/auth/";


    public boolean isTeacher(String email){
        String url = USER_SERVICE_DETAIL_URL + email;
        log.info("email: {}", email);
        UserDto user = restTemplate.getForObject(url, UserDto.class);
        log.info("Users: {}", user);
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("User not found or has no roles.");
            return false;
        }
        RoleDto role = user.getRoles().getFirst();
        return Objects.equals(role.getRoleName(), "teacher");
    }

    public boolean existByEmail(String email){
        String url =  USER_SERVICE_EXISTSBYEMAIL_URL+  email;
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }
    public UserDto getUserByEmail(String email){
        String url = USER_SERVICE_DETAIL_URL + email;
        return restTemplate.getForObject(url, UserDto.class);
    }

    public boolean isTokenBlacklisted(String token) {
        String url = AUTH_SERVICE_DETAIL_URL + "check-blacklist-token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, request, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

}
