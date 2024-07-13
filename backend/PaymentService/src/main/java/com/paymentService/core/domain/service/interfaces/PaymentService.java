package com.paymentService.core.domain.service.interfaces;

import com.paymentService.core.domain.model.CompletedOrder;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentOrder createPayment(BigDecimal sum, String email, Long orderId);

    Payment completePayment(String token);
}
