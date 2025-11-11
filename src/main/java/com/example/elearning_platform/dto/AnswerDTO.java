package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.DiscussionAnswer;
import lombok.Data;

import javax.swing.plaf.nimbus.State;
import java.time.LocalDateTime;

@Data
public class AnswerDTO {

    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;

    public static AnswerDTO fromEntity (DiscussionAnswer answer){
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setContent(answer.getContent());
        dto.setAuthorName(answer.getAuthor().getUsername());
        dto.setCreatedAt(answer.getCreatedAt());

        return dto;
    }
}
