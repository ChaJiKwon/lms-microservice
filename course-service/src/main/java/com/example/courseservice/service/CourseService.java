package com.example.courseservice.service;

import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseService {
     List<CourseDto> listAllCourse();
     List<CourseDto> listAllDeletedCourse();
     Course findById(Long id);
     CustomResponse createCourse(CourseDto courseDto);
     void delete(String courseName);
     void restoreCourse(String courseName);
     void updateCourse(Long id,Course course);
     CourseDto findByCourseName(String courseName);
     Boolean existByCourseName(String courseName);
}
