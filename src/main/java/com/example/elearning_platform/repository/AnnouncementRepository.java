package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // Find all announcements for a course, newest first
    List<Announcement> findByCourseIdOrderByCreatedAtDesc(Long courseId);
}
