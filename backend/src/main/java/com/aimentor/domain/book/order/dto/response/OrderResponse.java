package com.aimentor.domain.book.order.dto.response;

import com.aimentor.domain.book.order.entity.Order;
import com.aimentor.domain.book.order.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Integer totalPrice,
        OrderStatus status,
        LocalDateTime orderedAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse of(Order order, List<OrderItemResponse> items) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderedAt(),
                items
        );
    }
}
