package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository <Course, Long> {
}
