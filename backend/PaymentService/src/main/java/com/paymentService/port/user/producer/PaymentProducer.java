package com.paymentService.port.user.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.port.user.dto.PaymentCompletedDTO;
import com.paymentService.port.user.dto.mapper.PaymentCompletedDTOMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducer {

    @Value("payment")
    private String exchange;

    @Value("payment.payment.finished")
    private String finishedPaymentRoutingKey;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyPaymentCompleted(Payment payment) {
        PaymentCompletedDTO paymentCompletedDTO = PaymentCompletedDTOMapper.toDTO(payment);
        ObjectMapper objectMapper = new ObjectMapper();
        String message;
        try {
            message = objectMapper.writeValueAsString(paymentCompletedDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting PaymentCompletedDTO to JSON", e);
        }
        rabbitTemplate.convertAndSend(exchange, finishedPaymentRoutingKey, message);
    }
}
