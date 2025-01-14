package com.example.UserService.dto;

import com.example.UserService.Entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    List<Role> roles;
}
