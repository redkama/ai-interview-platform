package com.aimentor.domain.education.history.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HistoryQuizAnswerRequest(
        @NotNull(message = "quizId는 필수입니다.")
        Long quizId,
        @NotBlank(message = "selectedAnswer는 필수입니다.")
        String selectedAnswer
) {
}
