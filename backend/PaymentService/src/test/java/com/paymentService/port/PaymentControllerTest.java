package com.paymentService.port;

import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.interfaces.PaymentService;
import com.paymentService.port.user.controller.PaymentController;
import com.paymentService.port.user.producer.PaymentProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentProducer paymentProducer;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void createPayment_Success() throws Exception {
        PaymentOrder paymentOrder = new PaymentOrder("success", "paypalId", "redirectUrl");
        when(paymentService.createPayment(any(BigDecimal.class), anyString(), anyLong())).thenReturn(paymentOrder);

        mockMvc.perform(post("/paypal/init")
                        .param("sum", "100.00")
                        .param("email", "test@example.com")
                        .param("orderId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).createPayment(any(BigDecimal.class), anyString(), anyLong());
    }

    @Test
    void completePayment_Success() throws Exception {
        Payment payment = new Payment();
        payment.setStatus("success");
        when(paymentService.completePayment(anyString())).thenReturn(payment);

        mockMvc.perform(post("/paypal/capture")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).completePayment(anyString());
        verify(paymentProducer, times(1)).notifyOrderServicePaymentCompleted(any(Payment.class));
    }

    @Test
    void completePayment_Failure() throws Exception {
        Payment payment = new Payment();
        payment.setStatus("failure");
        when(paymentService.completePayment(anyString())).thenReturn(payment);

        mockMvc.perform(post("/paypal/capture")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).completePayment(anyString());
        verify(paymentProducer, times(0)).notifyOrderServicePaymentCompleted(any(Payment.class));
    }
}