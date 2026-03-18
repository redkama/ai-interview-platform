package com.aimentor.domain.book.book.dto.response;

import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;

public record BookSummaryResponse(
        Long id,
        String title,
        String author,
        String publisher,
        Integer price,
        Integer stock,
        String imageUrl,
        BookCategory category
) {
    public static BookSummaryResponse from(Book book) {
        return new BookSummaryResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getStock(),
                book.getImageUrl(),
                book.getCategory()
        );
    }
}
