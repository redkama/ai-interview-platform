package com.aimentor.domain.book.book.dto.response;

import java.util.List;

public record BookPageResponse(
        List<BookSummaryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
