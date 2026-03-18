package com.aimentor.domain.education.history.repository;

import com.aimentor.domain.education.history.entity.HistoryQuiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryQuizRepository extends JpaRepository<HistoryQuiz, Long> {

    List<HistoryQuiz> findAllByLessonIdOrderByIdAsc(Long lessonId);
}
