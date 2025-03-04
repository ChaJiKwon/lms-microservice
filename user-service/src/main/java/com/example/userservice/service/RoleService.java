package com.example.userservice.service;

import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.RoleInputDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DuplicateRoleException;
import com.example.userservice.exception.EmailNotFoundException;
import com.example.userservice.exception.InvalidRoleException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Set;
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    Set<String> validRoles = Set.of("admin", "student", "teacher");


    public UserDto setUserRole(RoleInputDto roleInputDto){
        User user = userRepository.findByEmail(roleInputDto.getEmail())
                .orElseThrow(()-> new EmailNotFoundException("Cannot found user with email" +roleInputDto.getEmail() ));

        if (!validRoles.contains(roleInputDto.getRoleName().toLowerCase())) {
            throw new InvalidRoleException("User can only have one of these roles: admin, student, teacher");
        }
        if (roleRepository.existsByUserId(user.getId())){
            throw new DuplicateRoleException("User already has role");
        }

        Role role = new Role();
        role.setRoleName(roleInputDto.getRoleName());
        role.setUser(user);
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName(role.getRoleName());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.getRoles().add(roleDto);
        log.info("Role: {}", role);
        roleRepository.save(role);
        return userDto;
    }


    public RoleInputDto editUserRole(RoleInputDto roleInputDto) {
        User user = userRepository.findByEmail(roleInputDto.getEmail())
                .orElseThrow(()-> new EmailNotFoundException("Cannot found user with email" +roleInputDto.getEmail() ));
        if (user == null) {
            throw new EmailNotFoundException("Cannot find user with email: " + roleInputDto.getEmail());
        }

        if (!validRoles.contains(roleInputDto.getRoleName().toLowerCase())) {
            throw new InvalidRoleException("User can only have one of these roles: admin, student, teacher");
        }
        Role role = roleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvalidRoleException("User does not have an existing role"));
        role.setRoleName(roleInputDto.getRoleName());
        roleRepository.save(role);
        RoleInputDto response= new RoleInputDto();
        response.setRoleName(role.getRoleName());
        response.setEmail(user.getEmail());
        return response;
    }
}
