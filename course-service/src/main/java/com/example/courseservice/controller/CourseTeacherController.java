package com.example.courseservice.controller;

import com.example.courseservice.dto.CourseTeacherInput;
import com.example.courseservice.dto.UserDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.service.CourseTeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("course-teacher")
@RequiredArgsConstructor
public class CourseTeacherController {
    private final CourseTeacherService courseTeacherService;
    @PostMapping("/add-teacher")
    public CustomResponse addTeacherToCourse(@RequestBody CourseTeacherInput input) {
        return courseTeacherService.addTeacherToCourse(input);
    }

    @DeleteMapping("/delete-teacher-from-course")
    public CustomResponse removeTeacherFromCourse( @RequestBody CourseTeacherInput input){
        return courseTeacherService.removeTeacherFromCourse(input);
    }


    @GetMapping("/teacher-list/{courseName}")
    public List<UserDto> getTeachersByCourse(@PathVariable("courseName") String courseName) {
        return  courseTeacherService.getTeachersByCourseName(courseName);
    }
}
