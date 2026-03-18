package com.aimentor.domain.book.book.controller;

import com.aimentor.common.api.ApiResponse;
import com.aimentor.common.security.AuthenticatedUser;
import com.aimentor.domain.book.book.dto.request.BookCreateRequest;
import com.aimentor.domain.book.book.dto.response.BookDetailResponse;
import com.aimentor.domain.book.book.dto.response.BookPageResponse;
import com.aimentor.domain.book.book.entity.BookCategory;
import com.aimentor.domain.book.book.service.BookService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books")
    public ApiResponse<BookPageResponse> getBooks(
            @RequestParam(required = false) BookCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.success(bookService.getBooks(category, page, size));
    }

    @GetMapping("/api/books/{id}")
    public ApiResponse<BookDetailResponse> getBook(@PathVariable Long id) {
        return ApiResponse.success(bookService.getBook(id));
    }

    @GetMapping("/api/books/search")
    public ApiResponse<BookPageResponse> searchBooks(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.success(bookService.searchBooks(query, page, size));
    }

    @PostMapping("/api/admin/books")
    public ApiResponse<BookDetailResponse> createBook(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody BookCreateRequest request
    ) {
        return ApiResponse.success(bookService.createBook(authenticatedUser.role(), request));
    }
}
