package com.aimentor.domain.education.english.dto.request;

import com.aimentor.domain.education.english.entity.EnglishLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnglishFeedbackRequest(
        @NotBlank(message = "userInput is required.")
        String userInput,

        @NotBlank(message = "expectedAnswer is required.")
        String expectedAnswer,

        @NotNull(message = "level is required.")
        EnglishLevel level
) {
}
