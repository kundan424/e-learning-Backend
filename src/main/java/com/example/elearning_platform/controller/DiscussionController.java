package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.*;
import com.example.elearning_platform.entity.DiscussionQuestion;
import com.example.elearning_platform.service.DiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping("/course/{courseId}/questions")
    @PreAuthorize("isAuthenticated()") // Any logged-in user can ask
    public ResponseEntity<QuestionSummaryDTO> askQuestion(
            @PathVariable Long courseId,
            @Valid @RequestBody QuestionRequest request
    ) {
        DiscussionQuestion newQuestion = discussionService.askQuestion(courseId, request);
        return ResponseEntity.ok(QuestionSummaryDTO.fromEntity(newQuestion));
    }

    @PostMapping("/questions/{questionId}/answers")
    @PreAuthorize("isAuthenticated()") // Any logged-in user can answer
    public ResponseEntity<AnswerDTO> postAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest request
    ) {
        AnswerDTO newAnswer = discussionService.postAnswer(questionId, request);
        return ResponseEntity.ok(newAnswer);
    }


    @GetMapping("/course/{courseId}/questions")
    public ResponseEntity<List<QuestionSummaryDTO>> getQuestionsForCourse(@PathVariable Long courseId) {
        List<QuestionSummaryDTO> questions = discussionService.getAllQuestionsForCourse(courseId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionDetailsDTO> getQuestionDetails(@PathVariable Long questionId) {
        QuestionDetailsDTO questionDetails = discussionService.getQuestionDetails(questionId);
        return ResponseEntity.ok(questionDetails);
    }
}
