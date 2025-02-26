package com.example.userservice.controller;

import com.example.userservice.dto.AuthDto;
import com.example.userservice.dto.PermissionInput;
import com.example.userservice.dto.RoleInputDto;
import com.example.userservice.entity.Permission;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;


import com.example.userservice.dto.UserDto;
import com.example.userservice.service.PermissionService;
import com.example.userservice.service.RoleService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;


    @GetMapping("/profile")
    public UserDto getUserProfile(@RequestHeader String email){
        return userService.getByEmail(email);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/deleted")
    public List<UserDto> getDeletedUsers(){
        return userService.getDeletedUsers();
    }
    @GetMapping("/detail/{id}")
    public UserDto getUserDetail(@PathVariable("id") Long id) {
       return userService.getUserById(id);
    }

    // use to send userdetail for auth-service
    @GetMapping("/{email}")
    public AuthDto sendUserDetail(@PathVariable("email") String email){
        return userService.sendUserDetailByEmail(email);
    }

    @GetMapping("/exists/{email}")
    public Boolean existByEmail(@PathVariable("email") String email){
        return userService.existsByEmail(email);
    }

    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable("email") String email){
        return userService.getByEmail(email);
    }
    @PutMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email){
        userService.delete(email);
        return new ResponseEntity<>("Delete user success",HttpStatus.OK);
    }
    @PutMapping("/restore/{email}")
    public ResponseEntity<String> restoreUser(@PathVariable("email") String email){
        userService.restoreUser(email);
        return new ResponseEntity<>("Restore user success",HttpStatus.OK);
    }

    //not in permission
    @PostMapping("/save")
    public User save(@RequestBody User user){
        return userService.save(user);
    }

    @PostMapping("/role")
    public ResponseEntity<?> setUserRole(@RequestBody RoleInputDto roleInputDto){
        Role role = roleService.setUserRole(roleInputDto);
        return new ResponseEntity<>(role,HttpStatus.OK);
    }

    @PutMapping("/role/edit")
    public ResponseEntity<?> editUserRole(@RequestBody RoleInputDto roleInputDto){
        return new ResponseEntity<>(roleService.editUserRole(roleInputDto),HttpStatus.OK);
    }


    // send to gate-way service
    @GetMapping("/check-permission")
    public ResponseEntity<Boolean> hasPermission(@RequestParam String path,
                                                 @RequestParam String method,
                                                 @RequestParam String role){
        boolean isAllowed = permissionService.hasPermission(path, method, role);
        return ResponseEntity.ok(isAllowed);
    }

    @PostMapping("/permission/give-permission")
    public ResponseEntity<Permission> giveUserPermission(@RequestBody PermissionInput permissionInput){
        Permission permission = permissionService.giveUserPermission(permissionInput.getPath()
                , permissionInput.getMethod()
                , permissionInput.getRoleName() );
        return new ResponseEntity<>(permission,HttpStatus.OK);
    }
}
