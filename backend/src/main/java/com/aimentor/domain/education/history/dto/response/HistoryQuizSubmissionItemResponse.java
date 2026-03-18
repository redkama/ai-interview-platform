package com.aimentor.domain.education.history.dto.response;

public record HistoryQuizSubmissionItemResponse(
        Long quizId,
        String selectedAnswer,
        String correctAnswer,
        boolean correct,
        String explanation
) {
}
