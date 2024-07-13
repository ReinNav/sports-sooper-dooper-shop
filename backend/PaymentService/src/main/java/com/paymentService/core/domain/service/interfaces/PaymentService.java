package com.paymentService.core.domain.service.interfaces;

import com.paymentService.core.domain.model.CompletedOrder;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentOrder createPayment(BigDecimal sum, String email, UUID orderId);

    Payment completePayment(String token);
}
