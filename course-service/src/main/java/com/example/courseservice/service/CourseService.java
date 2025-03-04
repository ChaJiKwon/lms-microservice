package com.example.courseservice.service;

import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface CourseService {
     Page<CourseDto> listAllCourse(final Pageable pageable,int currentPage);
     Page<CourseDto> listAllDeletedCourse(final Pageable pageable,int currentPage);
     Course findById(Long id);
     CustomResponse createCourse(CourseDto courseDto);
     void delete(String courseName);
     void restoreCourse(String courseName);
     void updateCourse(Long id,Course course);
     CourseDto findByCourseName(String courseName);
     Boolean existByCourseName(String courseName);
}
