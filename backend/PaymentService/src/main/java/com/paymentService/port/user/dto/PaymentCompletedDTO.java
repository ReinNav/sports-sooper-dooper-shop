package com.paymentService.port.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentCompletedDTO {
    private String paymentId;
    private UUID orderId;
    private UUID userId;
    private String status;
}