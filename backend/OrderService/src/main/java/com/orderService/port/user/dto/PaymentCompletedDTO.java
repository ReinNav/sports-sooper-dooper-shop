package com.orderService.port.user.dto;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class PaymentCompletedDTO {
    private String paymentId;
    private UUID orderId;
    private UUID userId;
    private String status;
}