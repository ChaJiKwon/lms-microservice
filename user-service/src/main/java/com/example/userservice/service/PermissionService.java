package com.example.userservice.service;

import com.example.userservice.entity.Permission;
import com.example.userservice.exception.InvalidRoleException;
import com.example.userservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private static final Set<String> VALID_ROLES = Set.of("admin", "teacher", "student");
    // check requets endpoints co pathvariable voi request param
    public boolean hasPermission(String path, String method, String roleName) {
        List<Permission> permissions = permissionRepository.findByMethodAndRoleName(method, roleName);

        // Lấy phần path chính, loại bỏ query params nếu có
        String cleanPath = path.split("\\?")[0];

        return permissions.stream()
                .map(permission -> {
                    // Chuẩn hóa path lưu trong DB để tạo regex khớp với thực tế
                    String permissionPathPattern = ".*" + permission.getPath().replaceAll("\\{[^}]+\\}", ".*") + ".*";
                    return Pattern.compile(permissionPathPattern);
                })
                .anyMatch(pattern -> pattern.matcher(cleanPath).matches());
    }

    public Permission giveUserPermission(String path, String method, String roleName) {
        method = method.toUpperCase();
        roleName = roleName.toLowerCase();

        if (!VALID_ROLES.contains(roleName)) {
            throw new InvalidRoleException("Invalid role: " + roleName);
        }

        Optional<Permission> existingPermission = permissionRepository.findByPathAndMethodAndRoleName(path, method, roleName);
        if (existingPermission.isPresent()) {
            return existingPermission.get();
        }
        Permission permission = new Permission();
        permission.setPath(path);
        permission.setMethod(method);
        permission.setRoleName(roleName);
        return permissionRepository.save(permission);
    }
}
