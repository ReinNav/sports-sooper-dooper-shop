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
            newCart.setCartItems(new HashMap<>());
            return cartRepository.save(newCart);
        });
    }

    @Override
    public Cart addToCart(UUID userId, CartItem cartItem, int quantity) {
        Cart cart = getCart(userId);
        CartItem itemToAdd;
        try {
            itemToAdd = cartItemService.findByProductId(cartItem.getProductId());

            int existingQuantity = cart.getCartItems().getOrDefault(itemToAdd, 0);
            cart.getCartItems().put(itemToAdd, existingQuantity + quantity);
        } catch (CartItemNotFound | NoSuchElementException e) {
            itemToAdd = cartItemRepository.save(cartItem);
            cart.getCartItems().put(itemToAdd, quantity);
        }

        cart.calculateTotalPriceAndItems();
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Cart subtractFromCart(UUID userId, CartItem cartItem, int quantity) throws CartItemNotFound{
        Cart cart = getCart(userId);
        CartItem itemToSubtract;

        try {
            itemToSubtract = cartItemService.findByProductId(cartItem.getProductId());

            Integer existingQuantity = cart.getCartItems().get(itemToSubtract);

            if (existingQuantity == null) {
                throw new NoSuchElementException();
            }

            cart.getCartItems().put(itemToSubtract, existingQuantity.intValue() - quantity);
        } catch (NoSuchElementException e) {
            throw new CartItemNotFound();
        }

        cart.calculateTotalPriceAndItems();
        cartRepository.save(cart);
        return cart;
    }
    @Override
    public Map.Entry<CartItem, Integer> removeFromCart(UUID userId, UUID productId) throws CartItemNotFound {
        Cart cart = getCart(userId);
        Map.Entry<CartItem, Integer> entryToRemove = null;

        for (Map.Entry<CartItem, Integer> entry : cart.getCartItems().entrySet()) {
            if (entry.getKey().getProductId().equals(productId)) {
                entryToRemove = entry;
                break;
            }
        }

        if (entryToRemove != null) {
            cart.getCartItems().remove(entryToRemove.getKey());
            cart.calculateTotalPriceAndItems();
            cartRepository.save(cart);
            return entryToRemove;
        } else {
            throw new CartItemNotFound();
        }
    }

    @Override
    public Cart clearCart(UUID userId) {
        Cart cart = getCart(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return cart;
    }
}
