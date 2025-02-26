package com.example.userservice.dto;
import lombok.Data;
import java.util.List;
@Data
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private List<RoleDto> roles;
}

