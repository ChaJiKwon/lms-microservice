package com.example.courseservice.service.impl;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.CourseTeacherInput;
import com.example.courseservice.dto.UserDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.CourseTeacher;
import com.example.courseservice.exception.*;
import com.example.courseservice.repository.CourseRepository;
import com.example.courseservice.repository.CourseTeacherRepository;
import com.example.courseservice.service.CourseTeacherService;
import com.example.courseservice.service.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseTeacherServiceImpl implements CourseTeacherService {
    private final UserServiceClient userServiceClient;
    private final CourseRepository courseRepository;
    private final CourseTeacherRepository courseTeacherRepository;
    @Override
    public CustomResponse addTeacherToCourse(CourseTeacherInput input) {
        //check course co ton tai ko
        if (!courseRepository.existsByCourseName(input.getCourseName())){
            throw new CourseNotFoundException("Course not found : " + input.getCourseName());
        }
        if (!userServiceClient.existByEmail(input.getTeacherEmail())){

            throw new TeacherNotExistException("Teacher " + input.getTeacherEmail() + " cannot found");
        }
        // check user tu user-service co phai giao vien ko
        if (!userServiceClient.isTeacher(input.getTeacherEmail())){
            throw new NotATeacherException("User is not a teacher !" );
        }

        // check giao vien da dc add vao course chua ?
        if (courseTeacherRepository.existsByCourseNameAndTeacherEmail(input.getCourseName(),input.getTeacherEmail())){
            throw new DuplicateTeacherException("teacher already in the course");
        }

        CourseTeacher courseTeacher = new CourseTeacher();
        courseTeacher.setCourseName(input.getCourseName());
        courseTeacher.setTeacherEmail(input.getTeacherEmail());
        courseTeacherRepository.save(courseTeacher);
        CustomResponse customResponse= new CustomResponse();
        customResponse.setMessage("Success add " + input.getTeacherEmail() + " to " + input.getCourseName() + " course");
        customResponse.setStatusCode(200);
        return  customResponse;
    }

    @Override
    public CustomResponse removeTeacherFromCourse(CourseTeacherInput input) {
        CustomResponse customResponse= new CustomResponse();
        CourseTeacher courseTeacher = courseTeacherRepository.findByCourseNameAndTeacherEmail(input.getCourseName(),input.getTeacherEmail())
                .orElseThrow(()->new NotInCourseException("Teacher is not in course"));
        courseTeacherRepository.delete(courseTeacher);
        customResponse.setMessage("Success delete " + input.getTeacherEmail() + " from " + input.getCourseName() + " course");
        customResponse.setStatusCode(200);
        return customResponse;
    }

    @Override
    public List<UserDto> getTeachersByCourseName(String courseName) {
        if (!courseRepository.existsByCourseName(courseName)) {
            throw new CourseNotFoundException("Course not found: " + courseName);
        }

        List<CourseTeacher> teacherEmails = courseTeacherRepository.findByCourseName(courseName)
                .orElse(new ArrayList<>());

        List<UserDto> teachers= new ArrayList<>();
        for (CourseTeacher teacher: teacherEmails){
            teachers.add(userServiceClient.getUserByEmail(teacher.getTeacherEmail()));
        }

        log.info("teachers{}", teachers);
        return teachers;
    }
}
