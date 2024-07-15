package com.cartService.port.user.controller;

import com.cartService.core.domain.model.Cart;
import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.interfaces.CartService;
import com.cartService.core.domain.service.interfaces.CartItemService;
import com.cartService.port.user.exception.InvalidQuantityException;
import com.cartService.port.user.producer.CartProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartProducer cartProducer;

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable UUID userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestParam UUID userId, @RequestBody CartItem cartItem, @RequestParam int quantity)
            throws InvalidQuantityException {
        if (quantity <= 0 || quantity == Integer.MAX_VALUE) {
            throw new InvalidQuantityException();
        }
        Cart cart = cartService.addToCart(userId, cartItem, quantity);
        cartProducer.changeProductAmount(cartItem, -quantity);
        return cart;
    }

    @PostMapping("/subtract")
    public Cart subtractFromCart(@RequestParam UUID userId, @RequestBody CartItem cartItem, @RequestParam int quantity)
            throws InvalidQuantityException, CartItemNotFound {
        if (quantity <= 0 || quantity == Integer.MAX_VALUE) {
            throw new InvalidQuantityException();
        }
        Cart cart = cartService.subtractFromCart(userId, cartItem, quantity);
        cartProducer.changeProductAmount(cartItem, quantity);
        return cart;
    }

    @PostMapping("/remove")
    public Cart removeFromCart(@RequestParam UUID userId, @RequestParam UUID productId) throws CartItemNotFound {
        CartItem removedFromCart = cartService.removeFromCart(userId, productId);
        cartProducer.changeProductAmount(removedFromCart, removedFromCart.getQuantity());
        return getCart(userId);
    }

    @PostMapping("/clear")
    public Cart clearCart(@RequestParam UUID userId) {
        Cart cart = cartService.getCart(userId);
        cart.getCartItems().forEach(item -> cartProducer.changeProductAmount(item, item.getQuantity()));
        return cartService.clearCart(userId);
    }
}
