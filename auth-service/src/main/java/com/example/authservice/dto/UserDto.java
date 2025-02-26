package com.example.authservice.dto;


import lombok.Data;

import java.util.List;


@Data
public class UserDto {
    private String email;
    private String username;

    private String password;
    private List<RoleDto> roles;
    private boolean isDelete;


}

