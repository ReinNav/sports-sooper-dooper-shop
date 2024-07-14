package com.orderService.port.user.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderService.core.domain.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusProducer {

    @Value("order")
    private String exchange;

    @Value("order.order.confirmed")
    private String orderConfirmedRoutingKey;

    @Value("order.payment.failed")
    private String paymentFailedRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderStatusProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyOrderConfirmed(Order order) {
        sendMessage(order, orderConfirmedRoutingKey);
    }

    public void notifyPaymentFailed(Order order) {
        sendMessage(order, paymentFailedRoutingKey);
    }

    private void sendMessage(Order order, String routingKey) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Order to JSON", e);
        }
    }
}
