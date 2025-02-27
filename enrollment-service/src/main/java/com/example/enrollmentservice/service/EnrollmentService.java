package com.example.enrollmentservice.service;


import com.example.enrollmentservice.dto.CourseDto;
import com.example.enrollmentservice.dto.UserDto;
import com.example.enrollmentservice.dto.response.CustomResponse;

import java.util.List;

public interface EnrollmentService {
    CustomResponse registerToCourse(String courseName,String coursePassword,String studentEmail);
    CustomResponse unEnrollFromCourse(String courseName, String studentEmail);
    List<UserDto> listStudentsInCourse(String courseName);

    List<CourseDto> listCoursesByStudent(String studentEmail);
}
