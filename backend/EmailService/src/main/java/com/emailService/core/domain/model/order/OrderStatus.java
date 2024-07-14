package com.emailService.core.domain.model.order;

public enum OrderStatus {
    PENDING,
    PAYMENT_FAILED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
