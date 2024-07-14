package com.paymentService.core.domain.service.impl;

import com.paymentService.core.domain.exception.PaymentCreationException;
import com.paymentService.core.domain.exception.PaymentNotFoundException;
import com.paymentService.core.domain.exception.PaymentCouldNotCompleteException;
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
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaypalService paypalService;

    @Override
    public PaymentOrder createPayment(BigDecimal amount, UUID userId, UUID orderId) {
        try {
            // create payment in paypal
            PaymentOrder paymentOrder = paypalService.createPayment(amount);

            // save payment entry
            Payment payment = new Payment();
            payment.setDate(new Date());
            payment.setAmount(amount);
            payment.setUserId(userId);
            payment.setStatus("Pending");
            payment.setOrderId(orderId);
            payment.setPaypalTransactionId(paymentOrder.getPayId());
            payment = paymentRepository.save(payment);

            return paymentOrder;

        } catch (Exception e) {
            throw new PaymentCreationException("Failed to create payment", e);
        }
    }

    @Override
    public Payment completePayment(String token) {
        try {
            // Call Paypal service to complete the payment
            CompletedOrder completedOrder = paypalService.completePayment(token);
            Payment payment = paymentRepository.findByPaypalTransactionId(completedOrder.getPayId());
            if (payment == null) {
                throw new PaymentNotFoundException("Payment with PayPal transaction ID " + completedOrder.getPayId() + " not found");
            }

            payment.setStatus(completedOrder.getStatus());
            paymentRepository.save(payment);

            return payment;

        } catch (PaymentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentCouldNotCompleteException("Failed to complete payment", e);
        }
    }
}
