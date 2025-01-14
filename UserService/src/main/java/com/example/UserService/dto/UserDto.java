package com.example.UserService.dto;

import com.example.UserService.Entity.Role;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private String email;
    private String username;
    private List<Role> roles;

    @JsonGetter("roles")
    public List<String> getRolesAsString() {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
}

