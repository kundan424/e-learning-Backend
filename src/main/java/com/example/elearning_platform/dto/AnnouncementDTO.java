package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Announcement;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnouncementDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String instructorName;
    private Long courseId;
    private String courseTitle;

    public  static AnnouncementDTO fromEntity (Announcement announcement){
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setContent(announcement.getContent());
        dto.setInstructorName(announcement.getInstructor().getUsername());
        dto.setCourseId(announcement.getCourse().getId());
        dto.setCourseTitle(announcement.getCourse().getTitle());

        return dto;
    }
}
