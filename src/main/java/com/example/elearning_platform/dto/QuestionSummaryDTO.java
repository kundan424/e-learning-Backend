package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.DiscussionQuestion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionSummaryDTO {

    private Long id;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private int answerCount;

    public static QuestionSummaryDTO fromEntity (DiscussionQuestion question){
        QuestionSummaryDTO dto = new QuestionSummaryDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setAuthorName(question.getAuthor().getUsername());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setAnswerCount(question.getAnswers().size());

        return dto;
    }

}
