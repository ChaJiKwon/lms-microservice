package com.example.enrollmentservice.repository;

import com.example.enrollmentservice.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean existsByCourseNameAndStudentEmail(String courseName, String studentEmail);
    Optional<Enrollment> findByCourseNameAndStudentEmail(String courseName, String studentEmail);
    List<Enrollment> findByCourseNameAndIsRegisteredTrue(String courseName);
    List<Enrollment> findByStudentEmailAndIsRegisteredTrue(String studentEmail);
}
