package com.orderService.domain.services;

import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.model.ShipmentType;
import com.orderService.core.domain.service.impl.OrderServiceImpl;
import com.orderService.core.domain.service.interfaces.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId(UUID.randomUUID());
        order.setDate("2023-07-13");
        order.setShipmentType(ShipmentType.DHL);
        order.setOrderItems(Collections.emptyList());
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetAllOrders() {
        UUID userId = order.getUserId();
        when(orderRepository.findByUserId(userId)).thenReturn(List.of(order));
        List<Order> orders = orderService.getOrdersByUserId(userId);
        assertFalse(orders.isEmpty());
        verify(orderRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetOrderById() {
        UUID orderId = order.getOrderId();
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));
        Optional<Order> foundOrder = orderService.getOrderById(orderId);
        assertTrue(foundOrder.isPresent());
        assertEquals(orderId, foundOrder.get().getOrderId());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order createdOrder = orderService.createOrder(order);
        assertNotNull(createdOrder.getOrderId());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateStatus() {
        UUID orderId = order.getOrderId();
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order updatedOrder = orderService.updateStatus(orderId, OrderStatus.CONFIRMED);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.CONFIRMED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).findByOrderId(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrder() {
        UUID orderId = order.getOrderId();
        doNothing().when(orderRepository).deleteById(orderId);
        orderService.deleteOrder(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
