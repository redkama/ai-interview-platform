package com.aimentor.domain.education.english.dto.response;

import com.aimentor.domain.education.english.entity.EnglishLearningHistory;
import java.time.LocalDateTime;

public record EnglishLearningHistoryResponse(
        Long id,
        Long lessonId,
        String lessonTitle,
        Integer score,
        LocalDateTime completedAt
) {
    public static EnglishLearningHistoryResponse from(EnglishLearningHistory history) {
        return new EnglishLearningHistoryResponse(
                history.getId(),
                history.getLesson().getId(),
                history.getLesson().getTitle(),
                history.getScore(),
                history.getCompletedAt()
        );
    }
}
