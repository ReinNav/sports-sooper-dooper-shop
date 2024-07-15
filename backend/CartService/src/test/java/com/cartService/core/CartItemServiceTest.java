package com.cartService.core;

import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.impl.CartItemServiceImpl;
import com.cartService.core.domain.service.interfaces.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartItemServiceTest {

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItem = new CartItem();
        cartItem.setProductId(UUID.randomUUID());
        cartItem.setName("Item 1");
        cartItem.setPrice(100.0f);
        cartItem.setGender("Female");
        cartItem.setSize("M");
        cartItem.setColour("Red");
        cartItem.setQuantity(1);
    }

    @Test
    public void testUpdateCartItem_ItemExists() throws CartItemNotFound {
        when(cartItemRepository.findByProductId(cartItem.getProductId())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem updatedItem = new CartItem();
        updatedItem.setProductId(cartItem.getProductId());
        updatedItem.setName("Updated Item");
        updatedItem.setPrice(150.0f);
        updatedItem.setGender("Male");
        updatedItem.setSize("L");
        updatedItem.setColour("Blue");

        CartItem result = cartItemService.updateCartItem(updatedItem);

        assertEquals("Updated Item", result.getName());
        assertEquals(150.0f, result.getPrice());
        assertEquals("Male", result.getGender());
        assertEquals("L", result.getSize());
        assertEquals("Blue", result.getColour());
        verify(cartItemRepository, times(1)).findByProductId(cartItem.getProductId());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    public void testUpdateCartItem_ItemDoesNotExist() {
        when(cartItemRepository.findByProductId(cartItem.getProductId())).thenReturn(Optional.empty());

        CartItem updatedItem = new CartItem();
        updatedItem.setProductId(cartItem.getProductId());
        updatedItem.setName("Updated Item");
        updatedItem.setPrice(150.0f);
        updatedItem.setGender("Male");
        updatedItem.setSize("L");
        updatedItem.setColour("Blue");

        assertThrows(CartItemNotFound.class, () -> cartItemService.updateCartItem(updatedItem));
        verify(cartItemRepository, times(1)).findByProductId(cartItem.getProductId());
        verify(cartItemRepository, times(0)).save(any(CartItem.class));
    }

    @Test
    public void testFindByProductId_ItemExists() throws CartItemNotFound {
        when(cartItemRepository.findByProductId(cartItem.getProductId())).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.findByProductId(cartItem.getProductId());

        assertNotNull(result);
        assertEquals(cartItem.getProductId(), result.getProductId());
        verify(cartItemRepository, times(1)).findByProductId(cartItem.getProductId());
    }

    @Test
    public void testFindByProductId_ItemDoesNotExist() {
        when(cartItemRepository.findByProductId(cartItem.getProductId())).thenReturn(Optional.empty());

        assertThrows(CartItemNotFound.class, () -> cartItemService.findByProductId(cartItem.getProductId()));
        verify(cartItemRepository, times(1)).findByProductId(cartItem.getProductId());
    }
}
