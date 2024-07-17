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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        when(paymentService.createPayment(any(BigDecimal.class), any(), any())).thenReturn(paymentOrder);

        mockMvc.perform(post("/paypal/init")
                        .param("sum", "100.00")
                        .param("userId", UUID.randomUUID().toString())
                        .param("orderId", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).createPayment(any(BigDecimal.class), any(), any());
    }

    @Test
    void completePayment_Success() throws Exception {
        Payment payment = new Payment();
        payment.setStatus("success");
        payment.setOrderId(UUID.randomUUID());
        when(paymentService.completePayment(anyString())).thenReturn(payment);

        mockMvc.perform(post("/paypal/capture")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.orderId").value(payment.getOrderId().toString()));

        verify(paymentService, times(1)).completePayment(anyString());
        verify(paymentProducer, times(1)).notifyPaymentCompleted(any(Payment.class));
    }

    @Test
    void completePayment_Failure() throws Exception {
        Payment payment = new Payment();
        payment.setStatus("failure");
        payment.setOrderId(UUID.randomUUID());
        when(paymentService.completePayment(anyString())).thenReturn(payment);

        mockMvc.perform(post("/paypal/capture")
                        .param("token", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.orderId").value(payment.getOrderId().toString()));

        verify(paymentService, times(1)).completePayment(anyString());
        verify(paymentProducer, times(1)).notifyPaymentCompleted(any(Payment.class));
    }
}
