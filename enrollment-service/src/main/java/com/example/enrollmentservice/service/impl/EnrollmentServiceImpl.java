package com.example.enrollmentservice.service.impl;

import com.example.enrollmentservice.dto.CourseDto;
import com.example.enrollmentservice.dto.UserDto;
import com.example.enrollmentservice.dto.response.CustomResponse;
import com.example.enrollmentservice.entity.Enrollment;
import com.example.enrollmentservice.exception.CourseNotFoundException;
import com.example.enrollmentservice.exception.DuplicateEnrollmentException;
import com.example.enrollmentservice.exception.InvalidCoursePasswordException;
import com.example.enrollmentservice.repository.EnrollmentRepository;
import com.example.enrollmentservice.service.EnrollmentService;
import com.example.enrollmentservice.service.client.CourseClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

//checked exception, unchecked exception

//auditing..
// swagger or openapi
//

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    @Override
    public CustomResponse registerToCourse(String courseName, String coursePassword,String studentEmail) {
        if (courseClient.existByCourseName(courseName)){
            throw new CourseNotFoundException("Course " + courseName + " cannot found");
        }
        String courseCode = generateCourseCode(courseName);
        if (!courseCode.equals(coursePassword)) {
            throw new InvalidCoursePasswordException("Incorrect course password! Please try again");
        }
        Optional<Enrollment> existingEnrollment = enrollmentRepository
                .findByCourseNameAndStudentEmail(courseName, studentEmail);


        CustomResponse response= new CustomResponse();
        if (existingEnrollment.isPresent()) {
            // Nếu đã có bản ghi, kiểm tra isRegistered
            if (existingEnrollment.get().isRegistered()) {
                throw new DuplicateEnrollmentException("Student " + studentEmail + " is already enrolled in " + courseName);
            } else {
                // Nếu isRegistered = false, cập nhật lại trạng thái thành true
                existingEnrollment.get().setRegistered(true);
                enrollmentRepository.save(existingEnrollment.get());
                response.setMessage("Student " + studentEmail.toUpperCase(Locale.ROOT) + " success register to course " + courseName );
                response.setStatusCode(200);
                return response;
            }
        }
        Enrollment enrollment =new Enrollment();
        enrollment.setCourseName(courseName);
        enrollment.setStudentEmail(studentEmail);
        enrollment.setRegistered(true);
        enrollmentRepository.save(enrollment);
        response.setMessage("Student " + studentEmail.toUpperCase(Locale.ROOT) + " success register to course " + courseName );
        response.setStatusCode(200);
        return response;
    }

    @Override
    public CustomResponse unEnrollFromCourse(String courseName, String studentEmail) {
        // Nếu đã unenroll trước đó
        if (courseClient.existByCourseName(courseName)){
            throw new CourseNotFoundException("Course " + courseName + " cannot found");
        }
        Enrollment enrollment = enrollmentRepository.findByCourseNameAndStudentEmail(courseName, studentEmail)
                .orElseThrow(()->new DuplicateEnrollmentException("Enrollment not found , please try again") );
        if (!enrollment.isRegistered()) {
            throw new DuplicateEnrollmentException("Student " + studentEmail + " has already unenrolled from " + courseName);
        }
        // Cập nhật trạng thái đăng ký
        enrollment.setRegistered(false);
        enrollmentRepository.save(enrollment);
        // Tạo response
        CustomResponse response = new CustomResponse();
        response.setMessage("Student " + studentEmail.toUpperCase(Locale.ROOT) + " has successfully unenrolled from course " + courseName);
        response.setStatusCode(200);
        return response;
    }

    @Override
    public List<UserDto> listStudentsInCourse(String courseName) {
        if (courseClient.existByCourseName(courseName)) {
            throw new CourseNotFoundException("Course " + courseName + " not found");
        }

        // Lấy danh sách email của sinh viên đã đăng ký khóa học
        List<String> studentEmails = enrollmentRepository.findByCourseNameAndIsRegisteredTrue(courseName)
                .stream()
                .map(Enrollment::getStudentEmail)
                .toList();

        // Gọi API hoặc repository để lấy thông tin chi tiết của từng sinh viên
        // Lấy thông tin user từ service hoặc database
        // Thêm danh sách khóa học vào Dto

        return studentEmails.stream().map(email -> {
            UserDto user = courseClient.getUserByEmail(email); // Lấy thông tin user từ service hoặc database
            List<String> registeredCourse = enrollmentRepository.findByStudentEmailAndIsRegisteredTrue(email)
                    .stream()
                    .map(Enrollment::getCourseName)
                    .toList();
            user.setEnrolledCourse(registeredCourse); // Thêm danh sách khóa học vào DTO
            return user;
        }).toList();
    }


    @Override
    public List<CourseDto> listCoursesByStudent(String studentEmail) {
        // Lấy danh sách tên khóa học mà sinh viên đã đăng ký
        List<String> courseNames = enrollmentRepository.findByStudentEmailAndIsRegisteredTrue(studentEmail)
                .stream()
                .map(Enrollment::getCourseName)
                .toList();

        // Gọi courseClient để lấy thông tin chi tiết từng khóa học
        return courseNames.stream().map(courseName -> {
            CourseDto courseDto = courseClient.getCourseByCourseName(courseName);

            // Gọi method để lấy danh sách giáo viên của khóa học
            List<UserDto> teachers = courseClient.getTeachersInCourse(courseName);

            // Trích xuất danh sách email từ danh sách giáo viên
            List<String> teacherEmails = teachers.stream()
                    .map(UserDto::getEmail) // Giả sử UserDto có thuộc tính `email`
                    .toList();

            // Gán danh sách email vào CourseDto
            courseDto.setTeachers(teacherEmails);

            return courseDto;
        }).toList();
    }




    // create password to enter course
    private String generateCourseCode(String courseName) {
        String abbreviation = Arrays.stream(courseName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase()) // Lấy chữ cái đầu
                .collect(Collectors.joining()); // Ghép lại thành chuỗi

        String year = String.valueOf(Year.now().getValue()); // Lấy năm hiện tại
        return abbreviation + year; // Ví dụ: ABC2025
    }
}
