package com.aimentor.domain.interview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InterviewQuestionCreateRequest(
        @NotBlank(message = "질문 내용은 필수입니다.")
        @Size(max = 1000, message = "질문 내용은 1000자 이하여야 합니다.")
        String content
) {
}
