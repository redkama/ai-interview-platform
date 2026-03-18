package com.aimentor.domain.book.book.dto.request;

import com.aimentor.domain.book.book.entity.BookCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookCreateRequest(
        @NotBlank(message = "title은 필수입니다.")
        String title,
        @NotBlank(message = "author는 필수입니다.")
        String author,
        @NotBlank(message = "publisher는 필수입니다.")
        String publisher,
        @NotNull(message = "price는 필수입니다.")
        @Min(value = 0, message = "price는 0 이상이어야 합니다.")
        Integer price,
        @NotNull(message = "stock은 필수입니다.")
        @Min(value = 0, message = "stock은 0 이상이어야 합니다.")
        Integer stock,
        @NotBlank(message = "description은 필수입니다.")
        String description,
        String imageUrl,
        @NotNull(message = "category는 필수입니다.")
        BookCategory category
) {
}
