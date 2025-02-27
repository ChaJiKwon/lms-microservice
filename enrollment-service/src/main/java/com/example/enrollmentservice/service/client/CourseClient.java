package com.example.enrollmentservice.service.client;

import com.example.enrollmentservice.dto.CourseDto;
import com.example.enrollmentservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseClient {
    private final RestTemplate restTemplate;

    public boolean existByCourseName(String courseName){
        String url = "http://course-service/course/exists/" + courseName;
        return !Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }
    public UserDto getUserByEmail(String email){
        String url = "http://user-service/user/email/" + email;
        return restTemplate.getForObject(url, UserDto.class);
    }


    public CourseDto getCourseByCourseName(String courseName){
        String url = "http://course-service/course/info/" + courseName;
        return restTemplate.getForObject(url, CourseDto.class);
    }

    public List<UserDto> getTeachersInCourse(String courseName) {
        String url = "http://course-service/course-teacher/teacher-list/" + courseName;

        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {}
        );

        return response.getBody(); // Trả về danh sách giáo viên
    }


}
