package com.orderService.port.user.controller;

import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.model.ShipmentType;
import com.orderService.core.domain.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId(userId);
        order.setDate("2023-07-13");
        order.setOrderItems(Collections.emptyList());
        order.setShipmentType(ShipmentType.STANDARD);
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetAllOrders() {
        when(orderService.getOrdersByUserId(userId)).thenReturn(List.of(order));
        List<Order> orders = orderController.getAllOrders(userId);
        assertFalse(orders.isEmpty());
        verify(orderService, times(1)).getOrdersByUserId(userId);
    }

    @Test
    void testGetOrderById() {
        UUID orderId = order.getOrderId();
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));
        ResponseEntity<Order> response = orderController.getOrderById(orderId);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(orderId, response.getBody().getOrderId());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testCreateOrder() {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        Order createdOrder = orderController.createOrder(order);
        assertNotNull(createdOrder.getOrderId());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testCancelOrder() {
        UUID orderId = order.getOrderId();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderService.updateStatus(orderId, OrderStatus.CANCELLED)).thenReturn(order);

        ResponseEntity<Order> response = orderController.cancelOrder(orderId);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(OrderStatus.CANCELLED, response.getBody().getStatus());
        verify(orderService, times(1)).updateStatus(orderId, OrderStatus.CANCELLED);
    }
}
