package com.aimentor.domain.book.cart.dto.response;

import java.util.List;

public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        Integer totalPrice
) {
}
