package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // get all notifications for a user , newest first
    List<Notification> findByUserIdOrderByCreatedAtDesc (Long userId);

    // Count unread notification
    long countByUserIdAndIsReadFalse (Long userId);
}
