package com.example.UserService.Controller;

import com.example.UserService.Entity.User;
import com.example.UserService.Service.AuthService;
import com.example.UserService.Service.UserService;
import com.example.UserService.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/all")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/detail/{id}")
    public Optional<UserDto> getUserDetail(@PathVariable Long id) {
        Optional<User> user = userService.getUserDetailForAuth(id);
        if (user.isEmpty()) {
            System.out.println("User not found for id: " + id);
        } else {
            System.out.println("User data: " + user.get());
        }
        UserDto userDto = new UserDto();
        userDto.setEmail(user.get().getEmail());
        userDto.setUsername(user.get().getUsername());
        userDto.setRoles(user.get().getRoles());
        System.out.println("roles: " + user.get().getRoles());
        return Optional.of(userDto);
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable("email") String email){
        return userService.findByEmail(email);
    }
    @GetMapping("/admin")
    public ResponseEntity<String> admin (){
            return new ResponseEntity<>("This is admin", HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<String> user (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.findByEmail(authentication.getName()).getUsername();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        GrantedAuthority role = roles.iterator().next();
        String response = "Welcome " + username + " entered" + ", Login as role of : " + role.toString();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/student")
    public ResponseEntity<String> directStudent (Authentication authentication){
        String username = userService.findByEmail(authentication.getName()).getUsername();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        GrantedAuthority role = roles.iterator().next();
        String response = "Welcome " + username + " entered" + ", Login as role of : " + role.toString();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
