package com.example.UserService;

import com.example.UserService.Entity.User;
import com.example.UserService.Service.UserService;
import com.example.UserService.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/all")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/detail/{id}")
    public Optional<User> getUserInfoForAuth(@PathVariable Long id) {
        Optional<User> user = userService.getUserDetailForAuth(id);
        if (user.isEmpty()) {
            System.out.println("User not found for id: " + id);
        } else {
            System.out.println("User data: " + user.get());
        }
        return user;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.addNewUser(user);
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable("email") String email){
        return userService.findByEmail(email);
    }
}
