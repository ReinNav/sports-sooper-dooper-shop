package com.cartService.core.domain.service.interfaces;

import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface CartItemService {
    CartItem updateCartItem(CartItem updatedItem) throws CartItemNotFound;
    CartItem findByProductId(UUID productId) throws CartItemNotFound;
}
