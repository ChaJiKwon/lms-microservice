package com.example.courseservice.service;

import com.example.courseservice.dto.CourseTeacherInput;
import com.example.courseservice.dto.UserDto;
import com.example.courseservice.dto.response.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseTeacherService {
     CustomResponse addTeacherToCourse(CourseTeacherInput input);
     CustomResponse removeTeacherFromCourse(CourseTeacherInput input);
     List<UserDto> getTeachersByCourseName(String  courseName);
}
