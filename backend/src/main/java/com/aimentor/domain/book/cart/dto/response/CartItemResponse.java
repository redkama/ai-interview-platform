package com.aimentor.domain.book.cart.dto.response;

import com.aimentor.domain.book.cart.entity.CartItem;

public record CartItemResponse(
        Long id,
        Long bookId,
        String title,
        Integer price,
        Integer quantity,
        Integer linePrice,
        String imageUrl
) {
    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getBook().getId(),
                cartItem.getBook().getTitle(),
                cartItem.getBook().getPrice(),
                cartItem.getQuantity(),
                cartItem.getBook().getPrice() * cartItem.getQuantity(),
                cartItem.getBook().getImageUrl()
        );
    }
}
