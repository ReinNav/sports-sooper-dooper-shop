package com.paymentService.port.user.dto;

import lombok.Data;

@Data
public class PaymentCompletedDTO {
    private String paymentId;
    private Long orderId;
    private String status;
}