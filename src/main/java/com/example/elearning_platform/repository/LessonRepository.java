package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
