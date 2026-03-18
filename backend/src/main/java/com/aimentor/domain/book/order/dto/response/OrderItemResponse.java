package com.aimentor.domain.book.order.dto.response;

import com.aimentor.domain.book.order.entity.OrderItem;

public record OrderItemResponse(
        Long id,
        Long bookId,
        String title,
        Integer quantity,
        Integer priceAtOrder,
        Integer linePrice
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getBook().getId(),
                orderItem.getBook().getTitle(),
                orderItem.getQuantity(),
                orderItem.getPriceAtOrder(),
                orderItem.getPriceAtOrder() * orderItem.getQuantity()
        );
    }
}
