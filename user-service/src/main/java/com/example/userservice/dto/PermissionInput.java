package com.example.userservice.dto;

import lombok.Data;

@Data
public class PermissionInput {
    private String path;
    private String method;
    private String roleName;
}
