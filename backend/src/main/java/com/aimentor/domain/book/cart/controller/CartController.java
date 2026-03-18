package com.aimentor.domain.book.cart.controller;

import com.aimentor.common.api.ApiResponse;
import com.aimentor.common.security.AuthenticatedUser;
import com.aimentor.domain.book.cart.dto.request.CartItemCreateRequest;
import com.aimentor.domain.book.cart.dto.request.CartItemUpdateRequest;
import com.aimentor.domain.book.cart.dto.response.CartResponse;
import com.aimentor.domain.book.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/api/cart")
    public ApiResponse<CartResponse> getCart(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return ApiResponse.success(cartService.getCart(authenticatedUser.userId()));
    }

    @PostMapping("/api/cart/items")
    public ApiResponse<CartResponse> addItem(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CartItemCreateRequest request
    ) {
        return ApiResponse.success(cartService.addItem(authenticatedUser.userId(), request));
    }

    @PatchMapping("/api/cart/items/{id}")
    public ApiResponse<CartResponse> updateItem(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id,
            @Valid @RequestBody CartItemUpdateRequest request
    ) {
        return ApiResponse.success(cartService.updateItem(authenticatedUser.userId(), id, request));
    }

    @DeleteMapping("/api/cart/items/{id}")
    public ApiResponse<Void> deleteItem(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        cartService.deleteItem(authenticatedUser.userId(), id);
        return ApiResponse.success();
    }
}
