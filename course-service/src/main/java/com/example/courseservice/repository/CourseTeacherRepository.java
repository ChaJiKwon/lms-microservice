package com.example.courseservice.repository;

import com.example.courseservice.entity.CourseTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTeacherRepository extends JpaRepository<CourseTeacher,Long> {
    boolean existsByCourseNameAndTeacherEmail(String courseName, String teacherEmail);
    Optional<CourseTeacher> findByCourseNameAndTeacherEmail(String courseName, String teacherEmail);
    Optional<List<CourseTeacher>> findByCourseName(String courseName);
}
