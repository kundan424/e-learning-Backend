package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Long> {

// check if specific user has completed a specific lesson
    boolean existsByUserIdAndLessonId (Long userId, Long lessonId);

//    Get all completed lesson IDs for a user within a specific course
    @Query("SELECT ulp.lesson.id FROM UserLessonProgress ulp " +
            "WHERE ulp.user.id = :userId AND ulp.lesson.course.id = :courseId")
    Set<Long> findCompletedLessonIdsByUserIdAndCourseId (Long userId, Long courseId);


}
