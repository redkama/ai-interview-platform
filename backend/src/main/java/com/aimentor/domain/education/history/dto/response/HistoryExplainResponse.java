package com.aimentor.domain.education.history.dto.response;

import com.aimentor.external.ai.dto.AiGenerateHistoryExplanationResponse;
import java.util.List;

public record HistoryExplainResponse(
        String explanation,
        List<String> keyPoints,
        String provider,
        boolean fallback
) {
    public static HistoryExplainResponse from(AiGenerateHistoryExplanationResponse response) {
        return new HistoryExplainResponse(
                response.explanation(),
                response.keyPoints(),
                response.provider(),
                response.fallback()
        );
    }
}
