package com.example.courseservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String email;
    private String username;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<RoleDto> roles;

}
