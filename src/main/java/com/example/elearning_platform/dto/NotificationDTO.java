package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Notification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private String link;
    private boolean isRead;
    private LocalDateTime createdAt;

    // A simple converter
    public static NotificationDTO fromEntity(Notification notif) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notif.getId());
        dto.setMessage(notif.getMessage());
        dto.setLink(notif.getLink());
        dto.setRead(notif.isRead());
        dto.setCreatedAt(notif.getCreatedAt());
        return dto;
    }
}
