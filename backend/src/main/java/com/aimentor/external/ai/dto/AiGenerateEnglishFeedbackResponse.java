package com.aimentor.external.ai.dto;

public record AiGenerateEnglishFeedbackResponse(
        boolean correct,
        int score,
        String explanation,
        String suggestion,
        String provider,
        boolean fallback
) {
}
