package com.example.userservice.repository;

import com.example.userservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    boolean existsByPathAndMethodAndRoleName(String path, String method, String roleName);
    Optional<Permission> findByPathAndMethodAndRoleName(String path, String method, String roleName);
    List<Permission> findByMethodAndRoleName(String method, String roleName);
}
