package com.orderService.port.user.dto;

import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.model.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private UUID userId;
    private String date;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;
    private ShippingAddressDTO shippingAddress;
    private BillingAddressDTO billingAddress;
    private ContactDetailsDTO contactDetails;
    private OrderStatus status;
    private ShipmentType shipmentType;
}
