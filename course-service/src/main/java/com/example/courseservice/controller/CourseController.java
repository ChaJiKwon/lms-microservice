package com.example.courseservice.controller;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import com.example.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @GetMapping("/all-course")
    public List<CourseDto> getAllCourses(){
        return courseService.listAllCourse();
    }
    @GetMapping("/deleted-course")
    public List<CourseDto> getDeletedCourses(){
        return courseService.listAllDeletedCourse();
    }

    @GetMapping("/info/{courseName}")
    public CourseDto getCourseByCourseName(@PathVariable("courseName") String courseName){
        return courseService.findByCourseName(courseName);
    }
    @PostMapping("/add")
    public CustomResponse addCourse(@RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }
    @PutMapping("/update/{id}")
    public void updateCourse(@PathVariable("id") Long id, @RequestBody Course course){
        courseService.updateCourse(id,course);
    }

    @PutMapping("/delete/{courseName}")
    public ResponseEntity<String> delete(@PathVariable("courseName") String courseName,@RequestHeader("email") String email ){
         courseService.delete(courseName);
        return new ResponseEntity<>("User " + email+" deleted course success", HttpStatus.OK);
    }

    @PutMapping("/restore/{courseName}")
    public ResponseEntity<String> restoreCourse(@PathVariable("courseName") String courseName){
        courseService.restoreCourse(courseName);
        return new ResponseEntity<>("Restore course success", HttpStatus.OK);
    }

    //called by enrollment-service
    @GetMapping("/exists/{courseName}")
    public Boolean existByCourseName(@PathVariable("courseName") String courseName){
        return courseService.existByCourseName(courseName);
    }
}
