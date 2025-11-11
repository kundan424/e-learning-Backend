package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.DiscussionQuestion;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionDetailsDTO {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private List<AnswerDTO> answers;

    public static QuestionDetailsDTO fromEntity (DiscussionQuestion question){
        QuestionDetailsDTO dto = new QuestionDetailsDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setAuthorName(question.getAuthor().getUsername());
        dto.setAnswers(
          question.getAnswers().stream()
                  .map(AnswerDTO::fromEntity)
                  .collect(Collectors.toList())
        );

        return dto;
    }
}
