package com.aimentor.domain.book.book.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.book.book.dto.request.BookCreateRequest;
import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;
import com.aimentor.domain.book.book.repository.BookRepository;
import com.aimentor.domain.user.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBookShouldRejectNonAdminRole() {
        BookCreateRequest request = new BookCreateRequest(
                "Book Title",
                "Author",
                "Publisher",
                10000,
                5,
                "Description",
                "https://example.com/book.jpg",
                BookCategory.OTHER
        );

        assertThatThrownBy(() -> bookService.createBook(Role.USER, request))
                .isInstanceOf(ApiException.class)
                .extracting("httpStatus", "errorCode")
                .containsExactly(HttpStatus.FORBIDDEN, "BOOK_ADMIN_FORBIDDEN");
    }

    @Test
    void createBookShouldPersistWhenAdmin() {
        BookCreateRequest request = new BookCreateRequest(
                "Book Title",
                "Author",
                "Publisher",
                10000,
                5,
                "Description",
                "https://example.com/book.jpg",
                BookCategory.OTHER
        );
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = bookService.createBook(Role.ADMIN, request);

        assertThat(response.title()).isEqualTo(request.title());
        assertThat(response.category()).isEqualTo(request.category());
        assertThat(response.price()).isEqualTo(request.price());
    }
}
