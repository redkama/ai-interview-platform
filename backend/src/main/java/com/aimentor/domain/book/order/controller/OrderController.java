package com.aimentor.domain.book.order.controller;

import com.aimentor.common.api.ApiResponse;
import com.aimentor.common.security.AuthenticatedUser;
import com.aimentor.domain.book.order.dto.response.OrderResponse;
import com.aimentor.domain.book.order.service.OrderService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ApiResponse<OrderResponse> createOrder(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return ApiResponse.success(orderService.createOrder(authenticatedUser.userId()));
    }

    @GetMapping("/api/orders/my")
    public ApiResponse<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return ApiResponse.success(orderService.getMyOrders(authenticatedUser.userId()));
    }

    @GetMapping("/api/orders/{id}")
    public ApiResponse<OrderResponse> getOrder(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        return ApiResponse.success(orderService.getOrder(authenticatedUser.userId(), id));
    }
}
