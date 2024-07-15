package com.orderService.core.domain.service.interfaces;

import com.orderService.core.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByOrderId(UUID orderId);
    List<Order> findByUserId(UUID userId);
}