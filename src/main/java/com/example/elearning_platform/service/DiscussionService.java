package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.*;
import com.example.elearning_platform.entity.Course;
import com.example.elearning_platform.entity.DiscussionAnswer;
import com.example.elearning_platform.entity.DiscussionQuestion;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.repository.CourseRepository;
import com.example.elearning_platform.repository.DiscussionAnswerRepository;
import com.example.elearning_platform.repository.DiscussionQuestionRepository;
import com.example.elearning_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionQuestionRepository questionRepository;
    private final DiscussionAnswerRepository answerRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @Transactional
    public DiscussionQuestion askQuestion(Long courseId, QuestionRequest request) {
        User author = getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        DiscussionQuestion question = new DiscussionQuestion();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setAuthor(author);
        question.setCourse(course);

        return questionRepository.save(question);
    }


    @Transactional
    public AnswerDTO postAnswer (Long questionId, AnswerRequest answerRequest){
        User author = getCurrentUser();
        DiscussionQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        DiscussionAnswer answer = new DiscussionAnswer();
        answer.setContent(answerRequest.getContent());
        answer.setAuthor(author);
        answer.setQuestion(question);

        DiscussionAnswer savedAnswer = answerRepository.save(answer);
        return AnswerDTO.fromEntity(savedAnswer);
    }

    @Transactional(readOnly = true)
    public List<QuestionSummaryDTO> getAllQuestionsForCourse (Long courseId){
        return questionRepository.findByCourseId(courseId).stream()
                .map(QuestionSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public QuestionDetailsDTO getQuestionDetails (Long questionId){
        DiscussionQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // We call fromEntity here, inside the @Transactional method,
        // so it can safely load the lazy-loaded answers.
        return QuestionDetailsDTO.fromEntity(question);
    }
}
