package com.aimentor.domain.book.order.repository;

import com.aimentor.domain.book.order.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrderIdOrderByIdAsc(Long orderId);
}
