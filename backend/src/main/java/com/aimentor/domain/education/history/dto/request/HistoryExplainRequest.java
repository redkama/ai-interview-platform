package com.aimentor.domain.education.history.dto.request;

import com.aimentor.domain.education.history.entity.HistoryEra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HistoryExplainRequest(
        @NotBlank(message = "topic is required.")
        String topic,

        @NotNull(message = "era is required.")
        HistoryEra era
) {
}
