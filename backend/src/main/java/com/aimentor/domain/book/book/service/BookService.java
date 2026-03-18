package com.aimentor.domain.book.book.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.book.book.dto.request.BookCreateRequest;
import com.aimentor.domain.book.book.dto.response.BookDetailResponse;
import com.aimentor.domain.book.book.dto.response.BookPageResponse;
import com.aimentor.domain.book.book.dto.response.BookSummaryResponse;
import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;
import com.aimentor.domain.book.book.repository.BookRepository;
import com.aimentor.domain.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookPageResponse getBooks(BookCategory category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books = category == null
                ? bookRepository.findAll(pageRequest)
                : bookRepository.findAllByCategory(category, pageRequest);
        return new BookPageResponse(
                books.getContent().stream().map(BookSummaryResponse::from).toList(),
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages()
        );
    }

    public BookDetailResponse getBook(Long bookId) {
        return BookDetailResponse.from(getBookEntity(bookId));
    }

    public BookPageResponse searchBooks(String query, int page, int size) {
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                query,
                query,
                query,
                PageRequest.of(page, size)
        );
        return new BookPageResponse(
                books.getContent().stream().map(BookSummaryResponse::from).toList(),
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages()
        );
    }

    @Transactional
    public BookDetailResponse createBook(Role role, BookCreateRequest request) {
        validateAdmin(role);
        Book book = bookRepository.save(Book.builder()
                .title(request.title())
                .author(request.author())
                .publisher(request.publisher())
                .price(request.price())
                .stock(request.stock())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .category(request.category())
                .build());
        return BookDetailResponse.from(book);
    }

    public Book getBookEntity(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "도서를 찾을 수 없습니다."));
    }

    private void validateAdmin(Role role) {
        if (role != Role.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "BOOK_ADMIN_FORBIDDEN", "관리자만 도서를 등록할 수 있습니다.");
        }
    }
}
