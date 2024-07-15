package com.orderService.core.domain.service.impl;

import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.service.interfaces.OrderRepository;
import com.orderService.core.domain.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public Order createOrder(Order order) {
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order updateStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findByOrderId(orderId).orElse(null);

        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }

        return null;
    }

    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
