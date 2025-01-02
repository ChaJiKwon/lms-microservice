package com.example.UserService.Repository;

import com.example.UserService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    void deleteUser(Long id);

    User updateUser(Long id, User userDetails);

    User creatUser(User user);
}
