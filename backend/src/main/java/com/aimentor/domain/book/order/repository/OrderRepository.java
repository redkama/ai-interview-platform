package com.aimentor.domain.book.order.repository;

import com.aimentor.domain.book.order.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserIdOrderByOrderedAtDescIdDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
