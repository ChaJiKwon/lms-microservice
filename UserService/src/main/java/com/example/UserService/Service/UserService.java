package com.example.UserService.Service;

import com.example.UserService.Entity.User;
import com.example.UserService.Repository.UserRepository;
import com.example.UserService.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<UserDTO> getAllUsers(){
        List<UserDTO>  usersInfos = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            usersInfos.add(userDTO);
        }
        return usersInfos;
    }
    public User addNewUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> getUserDetailForAuth(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
