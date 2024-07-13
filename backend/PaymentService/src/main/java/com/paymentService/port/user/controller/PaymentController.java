package com.paymentService.port.user.controller;

import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.interfaces.PaymentService;
import com.paymentService.port.user.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/paypal")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentProducer paymentProducer;

    @PostMapping(value = "/init")
    public PaymentOrder createPayment(
            @RequestParam("sum") BigDecimal sum,
            @RequestParam("userId") UUID userId,
            @RequestParam("orderId") UUID orderId) {
        return paymentService.createPayment(sum, userId, orderId);
    }

    @PostMapping(value = "/capture")
    public String completePayment(@RequestParam("token") String token) {
        Payment payment = paymentService.completePayment(token);
        paymentProducer.notifyPaymentCompleted(payment);
        return payment.getStatus();
    }
}
