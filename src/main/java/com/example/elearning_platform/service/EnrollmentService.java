package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.CourseResponseDTO;
import com.example.elearning_platform.entity.Course;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.repository.CourseRepository;
import com.example.elearning_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @Transactional
    public void enrollInCourse (Long courseId){
        User student = getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // check if already enrolled
        if (student.getEnrolledCourses().contains(course)){
            throw new IllegalArgumentException("You are already enrolled in this course");
        }

        // Enroll
        student.getEnrolledCourses().add(course);
        userRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getMyEnrolledCourses () {
        User student = getCurrentUser();

        Set<Course> enrolledCourses = student.getEnrolledCourses();

        return enrolledCourses.stream()
                .map(CourseResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
