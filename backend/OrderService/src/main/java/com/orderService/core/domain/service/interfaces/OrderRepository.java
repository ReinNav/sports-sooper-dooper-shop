package com.orderService.core.domain.service.interfaces;

import com.orderService.core.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Order findByOrderId(UUID orderId);
}