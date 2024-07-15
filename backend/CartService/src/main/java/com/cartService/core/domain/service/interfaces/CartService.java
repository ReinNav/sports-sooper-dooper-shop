package com.cartService.core.domain.service.interfaces;

import com.cartService.core.domain.model.Cart;
import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;

import java.util.Map;
import java.util.UUID;

public interface CartService {
    Cart getCart(UUID userId);
    Cart addToCart(UUID userId, CartItem cartItem, int quantity);

    Cart subtractFromCart(UUID userId, CartItem cartItem, int quantity) throws CartItemNotFound;

    CartItem removeFromCart(UUID userId, UUID productId) throws CartItemNotFound;

    Cart clearCart(UUID userId);
}

