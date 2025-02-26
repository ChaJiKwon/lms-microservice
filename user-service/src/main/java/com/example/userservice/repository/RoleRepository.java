package com.example.userservice.repository;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByUserId(Long userId);
    Optional<Role> findByUserId(Long id);


}
