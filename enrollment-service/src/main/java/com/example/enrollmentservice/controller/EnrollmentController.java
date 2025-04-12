package com.example.enrollmentservice.controller;


import com.example.enrollmentservice.dto.CourseDto;
import com.example.enrollmentservice.dto.CourseRegisterDto;
import com.example.enrollmentservice.dto.UserDto;
import com.example.enrollmentservice.dto.response.CustomResponse;
import com.example.enrollmentservice.service.EnrollmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Tag(
        name = "CRUD APIs for enrollment-service",
        description = "Enroll- unenroll students from a course, get all students in a specific courseName, and get all course registered by a specific student"

)
@RestController
@RequestMapping("enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public CustomResponse enrollToACourse(@RequestHeader("email") String studentEmail, @RequestBody CourseRegisterDto courseRegisterDto){
        return enrollmentService.registerToCourse(courseRegisterDto.getCourseName(), courseRegisterDto.getCoursePassword(), studentEmail);
    }

    @PostMapping("/unenroll")
    public CustomResponse unenrollFromCourse(@RequestHeader("email") String studentEmail, @RequestParam String courseName) {
        return enrollmentService.unEnrollFromCourse(courseName, studentEmail);
    }

    // Lấy danh sách sinh viên trong một khóa học
    @GetMapping("/course-students")
    public List<UserDto> getStudentsInCourse(@RequestParam String courseName) {
        return enrollmentService.listStudentsInCourse(courseName);
    }

    // Lấy danh sách khóa học mà sinh viên đã đăng ký
    @GetMapping("/registered-course")
    public List<CourseDto> getCoursesForStudent(@RequestHeader("email") String studentEmail) {
        return enrollmentService.listCoursesByStudent(studentEmail);
    }
}
