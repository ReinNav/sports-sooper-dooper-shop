package com.cartService.core;

import com.cartService.core.domain.model.Cart;
import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.impl.CartServiceImpl;
import com.cartService.core.domain.service.interfaces.CartItemRepository;
import com.cartService.core.domain.service.interfaces.CartItemService;
import com.cartService.core.domain.service.interfaces.CartRepository;
import com.cartService.port.user.exception.InvalidQuantityException;
import com.cartService.port.user.producer.CartProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartProducer cartProducer;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    private UUID userId;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        cartItem = new CartItem();
        cartItem.setProductId(UUID.randomUUID());
        cartItem.setName("Item 1");
        cartItem.setPrice(100.0f);
        cartItem.setGender("Female");
        cartItem.setSize("M");
        cartItem.setColour("Red");

        cart = new Cart();
        cart.setUserId(userId);
        cart.setCartItems(new HashMap<>());
    }

    @Test
    public void testGetCart_NewCartCreated() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCart_NewItem() throws CartItemNotFound {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemService.findByProductId(cartItem.getProductId())).thenThrow(new NoSuchElementException());
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addToCart(userId, cartItem, 1);

        assertEquals(1, result.getCartItems().size());
        assertEquals(1, result.getCartItems().get(cartItem));
    }

    @Test
    public void testAddToCart_ExistingItemInCartQuantityUpdated() throws CartItemNotFound {
        cart.getCartItems().put(cartItem, 2); // 2 items already in the cart
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemService.findByProductId(cartItem.getProductId())).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addToCart(userId, cartItem, 2);

        // One item added to cart
        assertEquals(1, result.getCartItems().size());
        // Quantity of the item in the cart updated by 2
        assertEquals(4, result.getCartItems().get(cartItem));
    }

    @Test
    public void testSubtractFromCart_ExistingItem() throws CartItemNotFound {
        cart.getCartItems().put(cartItem, 3); // 3 items in the cart
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemService.findByProductId(cartItem.getProductId())).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.subtractFromCart(userId, cartItem, 1);

        assertEquals(1, result.getCartItems().size());
        assertEquals(2, result.getCartItems().get(cartItem));
    }

    @Test
    public void testSubtractFromCart_ItemNotFound() throws CartItemNotFound {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemService.findByProductId(cartItem.getProductId())).thenThrow(new NoSuchElementException());

        assertThrows(CartItemNotFound.class, () -> cartService.subtractFromCart(userId, cartItem, 1));
    }

    @Test
    public void testRemoveFromCart_ItemExists() throws CartItemNotFound {
        cart.getCartItems().put(cartItem, 1);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Map.Entry<CartItem, Integer> removedItem = cartService.removeFromCart(userId, cartItem.getProductId());

        assertEquals(cartItem, removedItem.getKey());
        assertEquals(1, removedItem.getValue());
        assertTrue(cart.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testRemoveFromCart_ItemDoesNotExist() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        assertThrows(CartItemNotFound.class, () -> cartService.removeFromCart(userId, cartItem.getProductId()));
    }

    @Test
    public void testClearCart() {
        cart.getCartItems().put(cartItem, 1);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.clearCart(userId);

        assertTrue(result.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }
}
