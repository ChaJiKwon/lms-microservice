package com.example.authservice.service;
import com.example.authservice.dto.UserDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Component
public class UserService  {

    @Autowired
    private  RestTemplate restTemplate;
    public UserDto findByEmail(String email) {
        String userServiceUrl = "http://user-service/user/" + email; // URL của UserService
        UserDto userDto = restTemplate.getForObject(userServiceUrl, UserDto.class);
        log.info("user dto: " + userDto);
        return restTemplate.getForObject(userServiceUrl, UserDto.class);
    }
    public UserDto saveUser(UserDto userDTO){
        String url = "http://USER-SERVICE/user/save";
        UserDto request = new UserDto();
        request.setEmail(userDTO.getEmail());
        request.setUsername(userDTO.getUsername());
//        String password = userRegisterDTO.getPassword();
//        System.out.println("Paass: " + password);
        request.setPassword(userDTO.getPassword());
        return restTemplate.postForObject(url,request , UserDto.class);
    }
    public Boolean existsByEmail(String email) {
        UserDto user = findByEmail(email);
        return user != null;
    }
//    public static void main(String[] args) {
//        RestTemplate restTemplate1 = new RestTemplate();
//        String username = "user2";
//        UserClient uc = new UserClient(restTemplate1);
//        System.out.println(uc.getUserByUsername(username).getPassword());
//    }
}
