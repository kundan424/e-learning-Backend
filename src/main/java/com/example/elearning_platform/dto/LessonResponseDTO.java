package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Lesson;
import lombok.Data;

@Data
public class LessonResponseDTO {
    private Long id;
    private String title;
    private String contentText;
    private String videoUrl;

    public static LessonResponseDTO fromEntity(Lesson lesson){
        LessonResponseDTO dto = new LessonResponseDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContentText(lesson.getContentText());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}
