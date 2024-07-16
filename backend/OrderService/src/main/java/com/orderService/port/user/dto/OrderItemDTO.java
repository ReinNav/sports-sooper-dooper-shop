package com.orderService.port.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private UUID productId;
    private String productName;
    private String size;
    private String colour;
    private int quantity;
    private BigDecimal price;
}
