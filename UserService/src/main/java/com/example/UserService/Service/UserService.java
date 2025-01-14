package com.example.UserService.Service;

import com.example.UserService.Entity.User;
import com.example.UserService.Repository.UserRepository;
import com.example.UserService.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers(){
        List<UserDto>  usersInfos = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users){
            UserDto userDTO = new UserDto();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setRoles(user.getRoles());
            usersInfos.add(userDTO);
        }
        return usersInfos;
    }
    public Optional<User> getUserDetailForAuth(Long id){
        return userRepository.findById(id);
    }
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User save(User user){
        return userRepository.save(user);
    }
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
