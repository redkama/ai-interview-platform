package com.aimentor.domain.education.english.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EnglishLearningHistoryCreateRequest(
        @NotNull(message = "lessonId는 필수입니다.")
        Long lessonId,
        @NotNull(message = "score는 필수입니다.")
        @Min(value = 0, message = "score는 0 이상이어야 합니다.")
        @Max(value = 100, message = "score는 100 이하여야 합니다.")
        Integer score
) {
}
