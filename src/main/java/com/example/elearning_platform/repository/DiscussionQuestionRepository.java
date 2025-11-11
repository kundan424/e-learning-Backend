package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.DiscussionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionQuestionRepository extends JpaRepository<DiscussionQuestion, Long> {
    List<DiscussionQuestion> findByCourseId(Long courseId);
}
