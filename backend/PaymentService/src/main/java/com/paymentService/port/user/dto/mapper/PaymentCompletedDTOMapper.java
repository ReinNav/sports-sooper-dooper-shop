package com.paymentService.port.user.dto.mapper;

import com.paymentService.port.user.dto.PaymentCompletedDTO;
import com.paymentService.core.domain.model.Payment;

public class PaymentCompletedDTOMapper {

    public static PaymentCompletedDTO toDTO(Payment payment) {
        PaymentCompletedDTO dto = new PaymentCompletedDTO();
        dto.setPaymentId(payment.getPaypalTransactionId());
        dto.setOrderId(payment.getOrderId());
        dto.setStatus(payment.getStatus());
        dto.setUserId(payment.getUserId());
        return dto;
    }
}
