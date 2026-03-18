package com.aimentor.domain.book.cart.repository;

import com.aimentor.domain.book.cart.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartIdOrderByCreatedAtAscIdAsc(Long cartId);

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

    Optional<CartItem> findByCartIdAndBookId(Long cartId, Long bookId);

    void deleteAllByCartId(Long cartId);
}
