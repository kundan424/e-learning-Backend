package com.example.elearning_platform.controller;


import com.example.elearning_platform.dto.CourseResponseDTO;
import com.example.elearning_platform.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<?> enrollInCourse (@PathVariable Long courseId){
        try {
            enrollmentService.enrollInCourse(courseId);
            return ResponseEntity.ok().body("Successfully enrolled in course");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<?> getMyEnrolledCourses (){
        List<CourseResponseDTO> courses = enrollmentService.getMyEnrolledCourses();
        return ResponseEntity.ok(courses);
    }

}
