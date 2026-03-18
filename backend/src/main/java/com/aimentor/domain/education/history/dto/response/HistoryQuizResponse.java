package com.aimentor.domain.education.history.dto.response;

import com.aimentor.domain.education.history.entity.HistoryQuiz;

public record HistoryQuizResponse(
        Long id,
        Long lessonId,
        String question,
        String options
) {
    public static HistoryQuizResponse from(HistoryQuiz quiz) {
        return new HistoryQuizResponse(
                quiz.getId(),
                quiz.getLesson().getId(),
                quiz.getQuestion(),
                quiz.getOptions()
        );
    }
}
