package com.emailService.port.user.consumer;


import com.emailService.core.domain.model.order.Order;
import com.emailService.core.domain.services.interfaces.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer{

    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "order_confirmed_success")
    public void handleOrderConfirmed(String message) {
        try {
            Order order = objectMapper.readValue(message, Order.class);
            emailService.sendOrderConfirmationEmail(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

