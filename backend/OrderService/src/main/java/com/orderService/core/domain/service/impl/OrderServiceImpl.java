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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public Order createOrder(Order order) {
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order updateStatus(UUID orderId, OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElse(null);
    }

    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
