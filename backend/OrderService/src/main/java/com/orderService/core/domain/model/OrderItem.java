package com.orderService.core.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    private UUID productId;
    private String productName;
    private String size;
    private String colour;
    private int quantity;
    private BigDecimal price;
}
