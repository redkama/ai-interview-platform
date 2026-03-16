package com.aimentor.domain.interview.repository;

import com.aimentor.domain.interview.entity.InterviewSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    @EntityGraph(attributePaths = {"questions", "questions.answer", "feedback"})
    Optional<InterviewSession> findByIdAndUserId(Long id, Long userId);
}
