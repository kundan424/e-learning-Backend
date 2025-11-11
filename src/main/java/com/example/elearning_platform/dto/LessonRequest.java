package com.example.elearning_platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String contentText;
    private String videoUrl;
}