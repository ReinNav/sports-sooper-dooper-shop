package com.orderService.core.domain.model;

public enum OrderStatus {
    PENDING,
    PAYMENT_FAILED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
