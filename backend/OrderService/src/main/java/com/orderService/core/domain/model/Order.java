package com.orderService.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;
    private String userId;
    private String date;

    @ElementCollection
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;

    @ManyToOne
    private ShippingAddress shippingAddress;

    @ManyToOne
    private BillingAddress billingAddress;

    @Embedded
    private ContactDetails contactDetails;

    private OrderStatus status;

    private ShipmentType shipmentType;

}
