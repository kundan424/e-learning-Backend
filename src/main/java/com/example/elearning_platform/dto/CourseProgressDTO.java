package com.example.elearning_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CourseProgressDTO {

    private Long courseId;
    private int totalLessons;
    private int completedLessons;
    private Set<Long> completedLessonIds;
}
