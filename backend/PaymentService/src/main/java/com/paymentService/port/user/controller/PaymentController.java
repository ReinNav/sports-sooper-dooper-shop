package com.paymentService.port.user.controller;

import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.interfaces.PaymentService;
import com.paymentService.port.user.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
            @RequestParam("email") String email,
            @RequestParam("orderId") Long orderId) {
        return paymentService.createPayment(sum, email, orderId);
    }

    @PostMapping(value = "/capture")
    public String completePayment(@RequestParam("token") String token) {
        Payment payment = paymentService.completePayment(token);

        if (payment.getStatus().equals("success")) {
            // send message to order service to notify completion
            paymentProducer.notifyOrderServicePaymentCompleted(payment);
        }
        return payment.getStatus();
    }
}
