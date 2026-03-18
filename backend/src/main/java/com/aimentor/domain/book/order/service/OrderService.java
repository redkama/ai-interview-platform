package com.aimentor.domain.book.order.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.book.cart.entity.Cart;
import com.aimentor.domain.book.cart.entity.CartItem;
import com.aimentor.domain.book.cart.service.CartService;
import com.aimentor.domain.book.order.dto.response.OrderItemResponse;
import com.aimentor.domain.book.order.dto.response.OrderResponse;
import com.aimentor.domain.book.order.entity.Order;
import com.aimentor.domain.book.order.entity.OrderItem;
import com.aimentor.domain.book.order.entity.OrderStatus;
import com.aimentor.domain.book.order.repository.OrderItemRepository;
import com.aimentor.domain.book.order.repository.OrderRepository;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @Transactional
    public OrderResponse createOrder(Long userId) {
        User user = getUser(userId);
        Cart cart = cartService.getOrCreateCart(userId);
        List<CartItem> cartItems = cartService.getCartItems(cart.getId());
        if (cartItems.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "EMPTY_CART", "장바구니가 비어 있습니다.");
        }

        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBook().getStock() < cartItem.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BOOK_STOCK", "도서 재고가 부족합니다.");
            }
            totalPrice += cartItem.getBook().getPrice() * cartItem.getQuantity();
        }

        Order order = orderRepository.save(Order.builder()
                .user(user)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .orderedAt(LocalDateTime.now())
                .build());

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    cartItem.getBook().decreaseStock(cartItem.getQuantity());
                    return orderItemRepository.save(OrderItem.builder()
                            .order(order)
                            .book(cartItem.getBook())
                            .quantity(cartItem.getQuantity())
                            .priceAtOrder(cartItem.getBook().getPrice())
                            .build());
                })
                .toList();

        // TODO: integrate with a real payment provider and update order status after payment confirmation.
        cartService.clearCart(cart.getId());
        return OrderResponse.of(order, orderItems.stream().map(OrderItemResponse::from).toList());
    }

    public List<OrderResponse> getMyOrders(Long userId) {
        return orderRepository.findAllByUserIdOrderByOrderedAtDescIdDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."));
        return toResponse(order);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = orderItemRepository.findAllByOrderIdOrderByIdAsc(order.getId())
                .stream()
                .map(OrderItemResponse::from)
                .toList();
        return OrderResponse.of(order, items);
    }
}
