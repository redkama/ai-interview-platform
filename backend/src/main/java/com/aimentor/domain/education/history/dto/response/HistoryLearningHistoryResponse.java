package com.aimentor.domain.education.history.dto.response;

import com.aimentor.domain.education.history.entity.HistoryEra;
import com.aimentor.domain.education.history.entity.HistoryLearningHistory;
import java.time.LocalDateTime;

public record HistoryLearningHistoryResponse(
        Long id,
        Long lessonId,
        String lessonTitle,
        HistoryEra era,
        Integer quizScore,
        LocalDateTime completedAt
) {
    public static HistoryLearningHistoryResponse from(HistoryLearningHistory history) {
        return new HistoryLearningHistoryResponse(
                history.getId(),
                history.getLesson().getId(),
                history.getLesson().getTitle(),
                history.getLesson().getEra(),
                history.getQuizScore(),
                history.getCompletedAt()
        );
    }
}
