package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Course;
import lombok.Data;

@Data
public class CourseResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String instructorName;

    public static CourseResponseDTO fromEntity (Course course){
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructor().getUsername());
        return dto;
    }
}
