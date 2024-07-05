package com.paymentService.core.domain.service.impl;

import com.paymentService.core.domain.model.CompletedOrder;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.PaypalService;
import com.paymentService.core.domain.service.interfaces.PaymentRepository;
import com.paymentService.core.domain.service.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaypalService paypalService;

    @Override
    public PaymentOrder createPayment(BigDecimal amount, String email, Long orderId) {
        // Create Payment entity and save it
        Payment payment = new Payment();
        payment.setDate(new Date());
        payment.setAmount(amount);
        payment.setEmail(email);
        payment.setStatus("Pending");
        payment.setOrderId(orderId);
        payment = paymentRepository.save(payment);

        // Call Paypal service to create the payment
        PaymentOrder paymentOrder = paypalService.createPayment(amount);

        // Update Payment entity with the transaction id from Paypal
        payment.setPaypalTransactionId(paymentOrder.getPayId());
        paymentRepository.save(payment);

        return paymentOrder;
    }

    @Override
    public Payment completePayment(String token) {
        // Call Paypal service to complete the payment
        CompletedOrder completedOrder = paypalService.completePayment(token);

        // Update Payment entity based on the Paypal response
        Payment payment = paymentRepository.findByPaypalTransactionId(completedOrder.getPayId());
        if (payment != null) {
            payment.setStatus(completedOrder.getStatus());
            paymentRepository.save(payment);
        }

        return payment;
    }
}

