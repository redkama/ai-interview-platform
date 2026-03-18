package com.aimentor.domain.book.book.repository;

import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByCategory(BookCategory category, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String authorKeyword,
            String descriptionKeyword,
            Pageable pageable
    );
}
