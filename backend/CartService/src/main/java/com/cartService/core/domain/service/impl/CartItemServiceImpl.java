package com.cartService.core.domain.service.impl;

import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.interfaces.CartItemRepository;
import com.cartService.core.domain.service.interfaces.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(CartItem updatedItem) throws CartItemNotFound {
        Optional<CartItem> existingItem = cartItemRepository.findByProductId(updatedItem.getProductId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setName(updatedItem.getName());
            item.setPrice(updatedItem.getPrice());
            item.setGender(updatedItem.getGender());
            item.setSize(updatedItem.getSize());
            item.setColour(updatedItem.getColour());
            return cartItemRepository.save(item);
        } else {
            throw new CartItemNotFound();
        }
    }

    @Override
    public CartItem findByProductId(UUID productId) throws CartItemNotFound {
        return cartItemRepository.findByProductId(productId)
                .orElseThrow(() -> new CartItemNotFound());
    }
}
