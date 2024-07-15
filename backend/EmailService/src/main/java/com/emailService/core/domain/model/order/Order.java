package com.emailService.core.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    private UUID orderId;
    private String userId;
    private String date;
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    private ContactDetails contactDetails;
    private OrderStatus status;
    private ShipmentType shipmentType;
}
