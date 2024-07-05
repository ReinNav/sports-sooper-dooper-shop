package com.paymentService.core.domain.services;

import com.paymentService.core.domain.exception.PaymentCreationException;
import com.paymentService.core.domain.exception.PaymentNotFoundException;
import com.paymentService.core.domain.exception.PaymentCouldNotCompleteException;
import com.paymentService.core.domain.model.CompletedOrder;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.PaypalService;
import com.paymentService.core.domain.service.impl.PaymentServiceImpl;
import com.paymentService.core.domain.service.interfaces.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaypalService paypalService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_Success() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String email = "test@example.com";
        Long orderId = 1L;

        PaymentOrder paymentOrder = new PaymentOrder("success", "paypalId", "redirectUrl");
        when(paypalService.createPayment(any(BigDecimal.class))).thenReturn(paymentOrder);

        Payment payment = new Payment();
        payment.setPaypalTransactionId(paymentOrder.getPayId());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentOrder result = paymentService.createPayment(amount, email, orderId);

        assertNotNull(result);
        assertEquals("paypalId", result.getPayId());
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void createPayment_Failure() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String email = "test@example.com";
        Long orderId = 1L;

        when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(PaymentCreationException.class, () -> paymentService.createPayment(amount, email, orderId));
    }

    @Test
    void completePayment_Success() {
        String token = "token";
        CompletedOrder completedOrder = new CompletedOrder("success", "paypalId");
        when(paypalService.completePayment(anyString())).thenReturn(completedOrder);

        Payment payment = new Payment();
        payment.setPaypalTransactionId("paypalId");
        payment.setStatus("success");
        when(paymentRepository.findByPaypalTransactionId(anyString())).thenReturn(payment);

        Payment result = paymentService.completePayment(token);

        assertNotNull(result);
        assertEquals("success", result.getStatus());
        verify(paymentRepository, times(1)).findByPaypalTransactionId(anyString());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void completePayment_NotFound() {
        String token = "token";
        CompletedOrder completedOrder = new CompletedOrder("success", "paypalId");
        when(paypalService.completePayment(anyString())).thenReturn(completedOrder);

        when(paymentRepository.findByPaypalTransactionId(anyString())).thenReturn(null);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.completePayment(token));
    }

    @Test
    void completePayment_Failure() {
        String token = "token";
        CompletedOrder completedOrder = new CompletedOrder("success", "paypalId");
        when(paypalService.completePayment(anyString())).thenReturn(completedOrder);

        when(paymentRepository.findByPaypalTransactionId(anyString())).thenThrow(new RuntimeException("Database error"));

        assertThrows(PaymentCouldNotCompleteException.class, () -> paymentService.completePayment(token));
    }
}
