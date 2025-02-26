package com.example.userservice.service;
import com.example.userservice.dto.AuthDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DeletedUserException;
import com.example.userservice.exception.EmailNotFoundException;
import com.example.userservice.exception.IDNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> getAllUsers(){
        return userRepository.findByIsDeleteFalse().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
    public List<UserDto> getDeletedUsers(){
        return userRepository.findAll().stream()
                .filter(User::isDelete) // Lọc ra những user chưa bị xóa
                .map(user -> modelMapper.map(user, UserDto.class)) // Ánh xạ sang DTO
                .toList(); // Chuyển về List
    }

    public UserDto getUserById(Long id){
        if (userRepository.existsById(id)){
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IDNotFoundException("Cannot found id: " + id));
           return modelMapper.map(user, UserDto.class);
        }
        throw new RuntimeException("Id not exist");
    }
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User save(User user){
        return userRepository.save(user);
    }

    public void delete(String email){
        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IDNotFoundException("Cannot found email: " + email));
        if (user.isDelete()){
            throw new DeletedUserException("User already deleted");
        }
        user.setDelete(true);
        userRepository.save(user);
    }
    public void restoreUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IDNotFoundException("Cannot found email: " + email));
        if (!user.isDelete()){
            throw new DeletedUserException("User already is not deleted yet");
        }
        user.setDelete(false);
        userRepository.save(user);
    }

    // this function use to send password to auth-service, not for show user
    public AuthDto sendUserDetailByEmail(String email){
        if (!userRepository.existsByEmail(email)){
            return null;
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("cannot found user with email of: " + email));
        return modelMapper.map(user,AuthDto.class);
    }

    public UserDto getByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("cannot found user with email of: " + email));
        return modelMapper.map(user, UserDto.class);
    }
}
