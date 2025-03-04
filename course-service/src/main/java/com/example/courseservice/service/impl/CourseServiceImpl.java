package com.example.courseservice.service.impl;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.response.CustomResponse;
import com.example.courseservice.entity.Course;
import com.example.courseservice.exception.CourseNotFoundException;
import com.example.courseservice.exception.CourseServiceException;
import com.example.courseservice.exception.DateException;
import com.example.courseservice.exception.PageOutOfBoundException;
import com.example.courseservice.repository.CourseRepository;
import com.example.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    private Page<CourseDto> paginate(List<CourseDto> courseList,final Pageable pageable,int currentPage){
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), courseList.size());
        int totalPages = (int) Math.ceil((double) courseList.size() / pageable.getPageSize());
        if ( currentPage>= totalPages ){
            throw new PageOutOfBoundException("Page out of range, no more data left");
        }
        List<CourseDto> pagedCourses = courseList.subList(start, end);
        log.info("Total page , {}" ,totalPages);
        return new PageImpl<>(pagedCourses,  pageable, courseList.size());
    }



    @Override
    public Page<CourseDto> listAllCourse(final Pageable pageable,int currentPage) {
        List<CourseDto> filteredCourses = courseRepository.findAll()
                .stream()
                .filter(course -> !course.isDelete()) // Lọc những course chưa bị xóa
                .map(course -> modelMapper.map(course, CourseDto.class)) // Chuyển đổi sang DTO
                .toList();
        return paginate(filteredCourses,pageable,currentPage);
    }


    @Override
    public Page<CourseDto> listAllDeletedCourse(final Pageable pageable,int currentPage) {
        List<CourseDto> deletedCourse =  courseRepository.findAll()
                .stream()
                .filter(Course::isDelete)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
        return paginate(deletedCourse,pageable,currentPage);
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    @Override
    public CustomResponse createCourse(CourseDto courseDto) {
        if (courseDto.getStartDate().isAfter(courseDto.getEndDate())) {
            throw new DateException("Start date must be before end date.");
        }
        if (courseRepository.existsByCourseName(courseDto.getCourseName())) {
            throw new CourseServiceException("Duplicate course");
        }
        Course course = modelMapper.map(courseDto, Course.class);
        courseRepository.save(course);
        CustomResponse customResponse = new CustomResponse();
        customResponse.setMessage("Success create course " + course.getCourseName());
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
        Course course = courseRepository.findByCourseName(courseName).orElseThrow(() -> new CourseNotFoundException("Course " + courseName + " cannot found"));
        return modelMapper.map(course, CourseDto.class);
    }

    //called by enrollment service to check for data integrity
    @Override
    public Boolean existByCourseName(String courseName) {
        return courseRepository.existsByCourseName(courseName);
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
