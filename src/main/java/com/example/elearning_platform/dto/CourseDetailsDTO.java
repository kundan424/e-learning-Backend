package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Course;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CourseDetailsDTO {

    private Long id;
    private String title;
    private String description;
    private String instructorName;
    private List<LessonResponseDTO> lessons; // will include lesson here

    public static CourseDetailsDTO fromEntity(Course course) {
        CourseDetailsDTO dto = new CourseDetailsDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructor().getUsername());

        // Here we map the Set<Lesson> to a List<LessonResponseDTO>
        dto.setLessons(
                course.getLessons().stream()
                        .map(LessonResponseDTO::fromEntity)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
