package com.example.courseservice.service.impl;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import com.example.courseservice.exception.CourseNotFoundException;
import com.example.courseservice.exception.CourseServiceException;
import com.example.courseservice.exception.DateException;
import com.example.courseservice.repository.CourseRepository;
import com.example.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<CourseDto> listAllCourse() {
        return courseRepository.findAll()
                .stream()
                .filter(course -> !course.isDelete())
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
    }

    @Override
    public List<CourseDto> listAllDeletedCourse() {
        return courseRepository.findAll()
                .stream()
                .filter(Course::isDelete)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(()->new CourseNotFoundException("Course not found with id: " + id));
    }

    @Override
    public CustomResponse createCourse(CourseDto courseDto) {
        if (courseDto.getStartDate().isAfter(courseDto.getEndDate())) {
            throw new DateException("Start date must be before end date.");
        }
        if (courseRepository.existsByCourseName(courseDto.getCourseName())){
            throw new CourseServiceException("Duplicate course");
        }
        Course course = modelMapper.map(courseDto, Course.class);
        courseRepository.save(course);
        CustomResponse customResponse = new CustomResponse();
        customResponse.setMessage("Success create course " + course.getCourseName() );
        customResponse.setStatusCode(200);
        return customResponse;
    }


    @Override
    public void updateCourse(Long id,Course course){
        if (courseRepository.existsById(id)){
            course.setId(id);
            courseRepository.save(course);
        }
    }


    @Override
    public CourseDto findByCourseName(String courseName) {
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new CourseNotFoundException("Course " + courseName + " cannot found" ));
        return modelMapper.map(course, CourseDto.class);
    }

    @Override
    public void delete(String courseName) {
        //replace with deletebyId
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(()-> new CourseNotFoundException("Course not found: " + courseName));
        if (course.isDelete()){
            throw new CourseNotFoundException("Course:\"" + courseName + "\" is already deleted" );
        }
        course.setDelete(true);
        courseRepository.save(course);
    }

    @Override
    public void restoreCourse(String courseName){
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(()-> new CourseNotFoundException("Course not found: " + courseName));
        if (!course.isDelete()){
            throw new CourseNotFoundException("Course is not deleted yet: " + courseName );
        }
        course.setDelete(false);
        courseRepository.save(course);
    }
}
