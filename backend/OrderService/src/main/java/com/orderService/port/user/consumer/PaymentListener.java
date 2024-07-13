package com.orderService.port.user.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.service.impl.OrderServiceImpl;
import com.orderService.port.user.dto.PaymentCompletedDTO;
import com.orderService.port.user.producer.OrderStatusProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderStatusProducer orderStatusProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "payment_finished")
    public void handlePaymentCompleted(String message) {
        try {
            PaymentCompletedDTO paymentCompletedDTO = objectMapper.readValue(message, PaymentCompletedDTO.class);
            Order order = orderService.getOrderById(paymentCompletedDTO.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));

            if ("success".equalsIgnoreCase(paymentCompletedDTO.getStatus())) {
                order = orderService.updateStatus(paymentCompletedDTO.getOrderId(), OrderStatus.CONFIRMED);
                orderStatusProducer.notifyOrderConfirmed(order);
            } else {
                order = orderService.updateStatus(paymentCompletedDTO.getOrderId(), OrderStatus.PAYMENT_FAILED);
                orderStatusProducer.notifyPaymentFailed(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // eventually getting notification from warehouse or shipping service to update order status
    // @RabbitListener(queues = "warehouse")
    // public void handleWarehouseNotification(String message) {}




}
