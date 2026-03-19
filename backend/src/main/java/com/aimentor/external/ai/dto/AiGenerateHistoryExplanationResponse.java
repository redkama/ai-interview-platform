package com.aimentor.external.ai.dto;

import java.util.List;

public record AiGenerateHistoryExplanationResponse(
        String explanation,
        List<String> keyPoints,
        String provider,
        boolean fallback
) {
}
