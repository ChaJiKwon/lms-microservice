package com.example.courseservice.service.client;

import com.example.courseservice.dto.RoleDto;
import com.example.courseservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_DETAIL_URL = "http://user-service/user/email/";

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
        String url = "http://user-service/user/exist/" + email;
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }
    public UserDto getUserByEmail(String email){
        String url = USER_SERVICE_DETAIL_URL + email;
        return restTemplate.getForObject(url, UserDto.class);
    }
}
