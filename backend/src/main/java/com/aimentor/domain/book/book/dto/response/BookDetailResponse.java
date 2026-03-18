package com.aimentor.domain.book.book.dto.response;

import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;

public record BookDetailResponse(
        Long id,
        String title,
        String author,
        String publisher,
        Integer price,
        Integer stock,
        String description,
        String imageUrl,
        BookCategory category
) {
    public static BookDetailResponse from(Book book) {
        return new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getStock(),
                book.getDescription(),
                book.getImageUrl(),
                book.getCategory()
        );
    }
}
