package com.aimentor.domain.education.history.repository;

import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLesson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryLessonRepository extends JpaRepository<HistoryLesson, Long> {

    List<HistoryLesson> findAllByEraOrderByOrderNumAscIdAsc(HistoryEra era);
}
