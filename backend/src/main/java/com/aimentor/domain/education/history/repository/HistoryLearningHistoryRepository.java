package com.aimentor.domain.education.history.repository;

import com.aimentor.domain.education.history.entity.HistoryLearningHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryLearningHistoryRepository extends JpaRepository<HistoryLearningHistory, Long> {

    List<HistoryLearningHistory> findAllByUserIdOrderByCompletedAtDescIdDesc(Long userId);
}
