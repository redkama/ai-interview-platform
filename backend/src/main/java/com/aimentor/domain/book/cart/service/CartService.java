package com.aimentor.domain.book.cart.service;

import com.aimentor.common.exception.ApiException;
import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.service.BookService;
import com.aimentor.domain.book.cart.dto.request.CartItemCreateRequest;
import com.aimentor.domain.book.cart.dto.request.CartItemUpdateRequest;
import com.aimentor.domain.book.cart.dto.response.CartItemResponse;
import com.aimentor.domain.book.cart.dto.response.CartResponse;
import com.aimentor.domain.book.cart.entity.Cart;
import com.aimentor.domain.book.cart.entity.CartItem;
import com.aimentor.domain.book.cart.repository.CartItemRepository;
import com.aimentor.domain.book.cart.repository.CartRepository;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            BookService bookService
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    @Transactional
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return toResponse(cart, cartItemRepository.findAllByCartIdOrderByCreatedAtAscIdAsc(cart.getId()));
    }

    @Transactional
    public CartResponse addItem(Long userId, CartItemCreateRequest request) {
        Cart cart = getOrCreateCart(userId);
        Book book = bookService.getBookEntity(request.bookId());
        validateStock(book, request.quantity());

        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cart.getId(), book.getId())
                .map(existingItem -> {
                    validateStock(book, existingItem.getQuantity() + request.quantity());
                    existingItem.increaseQuantity(request.quantity());
                    return existingItem;
                })
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .cart(cart)
                        .book(book)
                        .quantity(request.quantity())
                        .build()));

        if (cartItem.getId() == null) {
            cartItem = cartItemRepository.save(cartItem);
        }
        return toResponse(cart, cartItemRepository.findAllByCartIdOrderByCreatedAtAscIdAsc(cart.getId()));
    }

    @Transactional
    public CartResponse updateItem(Long userId, Long cartItemId, CartItemUpdateRequest request) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = getOwnedCartItem(cart.getId(), cartItemId);
        validateStock(cartItem.getBook(), request.quantity());
        cartItem.changeQuantity(request.quantity());
        return toResponse(cart, cartItemRepository.findAllByCartIdOrderByCreatedAtAscIdAsc(cart.getId()));
    }

    @Transactional
    public void deleteItem(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.delete(getOwnedCartItem(cart.getId(), cartItemId));
    }

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .user(getUser(userId))
                        .build()));
    }

    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findAllByCartIdOrderByCreatedAtAscIdAsc(cartId);
    }

    @Transactional
    public void clearCart(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    private CartItem getOwnedCartItem(Long cartId, Long cartItemId) {
        return cartItemRepository.findByIdAndCartId(cartItemId, cartId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "장바구니 항목을 찾을 수 없습니다."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));
    }

    private void validateStock(Book book, int quantity) {
        if (book.getStock() < quantity) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BOOK_STOCK", "도서 재고가 부족합니다.");
        }
    }

    private CartResponse toResponse(Cart cart, List<CartItem> cartItems) {
        List<CartItemResponse> items = cartItems.stream()
                .map(CartItemResponse::from)
                .toList();
        int totalPrice = items.stream()
                .mapToInt(CartItemResponse::linePrice)
                .sum();
        return new CartResponse(cart.getId(), items, totalPrice);
    }
}
