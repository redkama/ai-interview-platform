package com.aimentor.external.ai.dto;

public record AiGenerateEnglishFeedbackRequest(
        String userInput,
        String expectedAnswer,
        String level
) {
}
