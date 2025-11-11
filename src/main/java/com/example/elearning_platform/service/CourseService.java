package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.*;
import com.example.elearning_platform.entity.Course;
import com.example.elearning_platform.entity.Lesson;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.repository.CourseRepository;
import com.example.elearning_platform.repository.LessonRepository;
import com.example.elearning_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    private User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // course methods

    @Transactional
    public CourseResponseDTO createCourse (CourseRequest request){
        User instructor = getCurrentUser();

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(instructor);

        Course saved = courseRepository.save(course);
        return CourseResponseDTO.fromEntity(saved);
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseDetailsDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // By calling course.getLessons() *inside* a @Transactional method,
        // we can safely load the lazy-loaded lessons before we convert to a DTO.
        return CourseDetailsDTO.fromEntity(course);
    }

    public Lesson addLessonToCourse (Long courseId, LessonRequest request){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));


        User currentUser = getCurrentUser();
        if (!course.getInstructor().getId().equals(currentUser.getId())){
            throw new SecurityException("You are not authorized to add lessons to this course");
        }
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContentText(request.getContentText());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setCourse(course);

        return lessonRepository.save(lesson);

    }

    public LessonResponseDTO getLessonDetails (Long courseId, Long lessonId){
        User currentUser = getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean isInstructor = course.getInstructor().getId().equals(currentUser.getId());
        boolean isEnrolled = currentUser.getEnrolledCourses().contains(course);

        if (!isInstructor && !isEnrolled){
            throw new SecurityException("You are not authorized to view this lesson");
        }

        // find the lesson
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // check if the lesson actually belongs to the course
        if (!lesson.getCourse().getId().equals(courseId)){
            throw new RuntimeException("Lesson does not belong to this course");
        }

        return LessonResponseDTO.fromEntity(lesson);
    }
}
