package com.aimentor.domain.education.history.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record HistoryQuizSubmitRequest(
        @NotNull(message = "lessonId는 필수입니다.")
        Long lessonId,
        @NotEmpty(message = "answers는 비어 있을 수 없습니다.")
        List<@Valid HistoryQuizAnswerRequest> answers
) {
}
