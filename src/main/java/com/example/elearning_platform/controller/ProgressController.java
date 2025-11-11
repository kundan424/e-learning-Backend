package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.CourseProgressDTO;
import com.example.elearning_platform.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('STUDENT')")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/lesson/{lessonId}/complete")
    public ResponseEntity<?> markLessonComplete (@PathVariable Long lessonId){
        try {
            progressService.markLessonAsComplete(lessonId);
            return ResponseEntity.ok().body("Lesson marked as complete");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseProgressDTO> getCourseProgress (@PathVariable Long courseId){
        CourseProgressDTO progress = progressService.getCourseProgress(courseId);
        return ResponseEntity.ok(progress);
    }

}
