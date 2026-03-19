package com.aimentor.domain.education.english.dto.response;

import com.aimentor.external.ai.dto.AiGenerateEnglishFeedbackResponse;

public record EnglishFeedbackResponse(
        boolean correct,
        int score,
        String explanation,
        String suggestion,
        String provider,
        boolean fallback
) {
    public static EnglishFeedbackResponse from(AiGenerateEnglishFeedbackResponse response) {
        return new EnglishFeedbackResponse(
                response.correct(),
                response.score(),
                response.explanation(),
                response.suggestion(),
                response.provider(),
                response.fallback()
        );
    }
}
