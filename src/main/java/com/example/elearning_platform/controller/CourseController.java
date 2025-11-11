package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.*;
import com.example.elearning_platform.entity.*;
import com.example.elearning_platform.repository.AnnouncementRepository;
import com.example.elearning_platform.repository.CourseRepository;
import com.example.elearning_platform.repository.NotificationRepository;
import com.example.elearning_platform.repository.UserRepository;
import com.example.elearning_platform.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final NotificationRepository notificationRepository;
    private final AnnouncementRepository announcementRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponseDTO course = courseService.createCourse(request);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/lessons")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    public ResponseEntity<LessonResponseDTO> addLessonToCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonRequest request) {
        Lesson lesson = courseService.addLessonToCourse(courseId, request);
        LessonResponseDTO response = LessonResponseDTO.fromEntity(lesson);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailsDTO> getCourseById(@PathVariable Long courseId) {
        CourseDetailsDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<?> getLessonDetails(
            @PathVariable Long courseId,
            @PathVariable Long lessonId
    ) {
        try {
            LessonResponseDTO lesson = courseService.getLessonDetails(courseId, lessonId);
            return ResponseEntity.ok(lesson);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage()); // 403 Forbidden
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage()); // 404 Not Found
        }
    }

    @PostMapping("/{courseId}/announce")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @Transactional
    public ResponseEntity<?> makeAnnouncement(
            @PathVariable Long courseId,
            @RequestBody String announcementContent,
            Authentication authentication
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        String username = authentication.getName();
        User instructor = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Step 1: Create the source Announcement
        Announcement announcement = new Announcement();
        announcement.setCourse(course);
        announcement.setInstructor(instructor);
        announcement.setContent(announcementContent);
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        String link = "/announcements/" + savedAnnouncement.getId(); // Link to the new post
        String message = "New announcement in " + course.getTitle();

        // Step 2: Create Notification pointers
        for (User student : course.getStudents()) {
            Notification notif = new Notification();
            notif.setUser(student);
            notif.setMessage(message);
            notif.setLink(link);
            Notification savedNotif = notificationRepository.save(notif);

            NotificationDTO dto = NotificationDTO.fromEntity(savedNotif);

            // Step 3: Send real-time ping
            messagingTemplate.convertAndSendToUser(
                    student.getUsername(),
                    "/queue/notifications",
                    dto
            );
        }

        return ResponseEntity.ok(savedAnnouncement); // Return the new announcement
    }
}
