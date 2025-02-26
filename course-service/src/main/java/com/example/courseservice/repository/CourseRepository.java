package com.example.courseservice.repository;

import com.example.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByCourseName(String courseName);
    Optional<Course> findByCourseName(String courseName);
}
