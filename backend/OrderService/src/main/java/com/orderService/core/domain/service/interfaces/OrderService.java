package com.orderService.core.domain.service.interfaces;

import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    List<Order> getOrdersByUserId(UUID userId);

    Optional<Order> getOrderById(UUID orderId);

    Order createOrder(Order order);

    Order updateStatus(UUID orderId, OrderStatus status);

    void deleteOrder(UUID orderId);
}
