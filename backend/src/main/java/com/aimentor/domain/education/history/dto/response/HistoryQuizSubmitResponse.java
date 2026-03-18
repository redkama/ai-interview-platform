package com.aimentor.domain.education.history.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record HistoryQuizSubmitResponse(
        Long historyId,
        Long lessonId,
        Integer score,
        LocalDateTime completedAt,
        List<HistoryQuizSubmissionItemResponse> results
) {
}
