package com.cartService.port.dto;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class PaymentCompletedDTO {
    private String paymentId;
    private UUID orderId;
    private String status;
    private UUID userId;
}
