package com.emailService.core.domain.model.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class Order {

    private UUID orderId;
    private UUID userId;
    private String date;
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    private ContactDetails contactDetails;
    private OrderStatus status;
    private ShipmentType shipmentType;
}
