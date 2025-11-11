package com.example.elearning_platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {

    @NotBlank(message = "Answer content cannot be blank")
    private String content;

}
