package com.cartService.core.domain.service.impl;

import com.cartService.core.domain.model.Cart;
import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.interfaces.CartItemRepository;
import com.cartService.core.domain.service.interfaces.CartRepository;
import com.cartService.core.domain.service.interfaces.CartService;
import com.cartService.core.domain.service.interfaces.CartItemService;
import com.cartService.port.user.producer.CartProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartProducer cartProducer;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setCartItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });
    }

    @Override
    public Cart addToCart(UUID userId, CartItem cartItem, int quantity) {
        Cart cart = getCart(userId);
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }

        cart.calculateTotalPriceAndItems();
        return cartRepository.save(cart);
    }

    @Override
    public Cart subtractFromCart(UUID userId, CartItem cartItem, int quantity) throws CartItemNotFound {
        Cart cart = getCart(userId);
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() - quantity);

            if (item.getQuantity() <= 0) {
                cart.getCartItems().remove(item);
            }
        } else {
            throw new CartItemNotFound();
        }

        cart.calculateTotalPriceAndItems();
        return cartRepository.save(cart);
    }

    @Override
    public CartItem removeFromCart(UUID userId, UUID productId) throws CartItemNotFound {
        Cart cart = getCart(userId);
        Optional<CartItem> itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (itemToRemove.isPresent()) {
            cart.getCartItems().remove(itemToRemove.get());
            cart.calculateTotalPriceAndItems();

            cartRepository.save(cart);
            return itemToRemove.get();
        } else {
            throw new CartItemNotFound();
        }
    }

    @Override
    public Cart clearCart(UUID userId) {
        Cart cart = getCart(userId);
        cart.getCartItems().clear();
        cart.calculateTotalPriceAndItems();
        cartRepository.save(cart);
        return cart;
    }
}
