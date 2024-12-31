package com.example.AuthService.Client;
import com.example.AuthService.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserService  {
    private  RestTemplate restTemplate;

    private PasswordEncoder encoder;
    @Autowired
    public UserService(RestTemplate restTemplate, PasswordEncoder encoder) {
        this.restTemplate = restTemplate;
        this.encoder = encoder;
    }
    public UserDto getUserByUsername(String username) {
        String userServiceUrl = "http://localhost:8080/users/" + username; // URL của UserService
        return restTemplate.getForObject(userServiceUrl, UserDto.class);
    }

    public UserDto registerNewUser(UserDto userDTO){
        String url = "http://localhost:8080/users/register";
        UserDto request = new UserDto();
        request.setEmail(userDTO.getEmail());
        request.setUsername(userDTO.getUsername());
        request.setPassword(encoder.encode(userDTO.getPassword()));
        return restTemplate.postForObject(url,request , UserDto.class);
    }
//    public static void main(String[] args) {
//        RestTemplate restTemplate1 = new RestTemplate();
//        String username = "user2";
//        UserClient uc = new UserClient(restTemplate1);
//        System.out.println(uc.getUserByUsername(username).getPassword());
//    }
}
