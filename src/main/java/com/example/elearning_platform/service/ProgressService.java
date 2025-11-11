package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.CourseProgressDTO;
import com.example.elearning_platform.entity.Course;
import com.example.elearning_platform.entity.Lesson;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.entity.UserLessonProgress;
import com.example.elearning_platform.repository.CourseRepository;
import com.example.elearning_platform.repository.LessonRepository;
import com.example.elearning_platform.repository.UserLessonProgressRepository;
import com.example.elearning_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserLessonProgressRepository progressRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @Transactional
    public void markLessonAsComplete (Long lessonId){
        User user = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // check : if user is enrolled in course
        Course course = lesson.getCourse();
        if (!user.getEnrolledCourses().contains(course)){
            throw new SecurityException("You are not enrolled in this course");
        }

        if (progressRepository.existsByUserIdAndLessonId(user.getId(), lessonId)){
            throw new IllegalArgumentException("Lesson already marked as complete");
        }

        UserLessonProgress progress = new UserLessonProgress();
        progress.setUser(user);
        progress.setLesson(lesson);
        progressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public CourseProgressDTO getCourseProgress (Long courseId){
        User user = getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int totalLessons = course.getLessons().size();

        // 2. Get the IDs of completed lessons for this user and course
        Set<Long> completedLessonIds = progressRepository
                .findCompletedLessonIdsByUserIdAndCourseId(user.getId(), courseId);

        // create and return the DTO
        return new CourseProgressDTO(
          courseId,
          totalLessons,
          completedLessonIds.size(),
                completedLessonIds
        );
    }
}
