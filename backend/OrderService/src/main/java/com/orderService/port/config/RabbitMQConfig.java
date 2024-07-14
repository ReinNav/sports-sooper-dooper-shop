package com.orderService.port.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("payment");
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order");
    }

    @Bean
    public Queue paymentFinishedQueue() {
        return new Queue("payment_finished");
    }

    @Value("payment.payment.finished")
    private String finishedPaymentRoutingKey;

    @Value("order.order.confirmed")
    private String orderConfirmedRoutingKey;

    @Value("order.payment.failed")
    private String orderPaymentFailedRoutingKey;

    @Bean
    public Queue orderConfirmedSuccessQueue() {
        return new Queue("order_confirmed_success");
    }

    @Bean
    public Queue orderPaymentFailedQueue() {
        return new Queue("order_payment_failed");
    }

    @Bean
    public Queue paymentFailedQueue() {
        return new Queue("payment_failed");
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(orderConfirmedSuccessQueue())
                .to(orderExchange())
                .with(orderConfirmedRoutingKey);
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder
                .bind(orderPaymentFailedQueue())
                .to(orderExchange())
                .with(orderPaymentFailedRoutingKey);
    }

    @Bean
    public Binding binding3(){
        return BindingBuilder
                .bind(paymentFinishedQueue())
                .to(paymentExchange())
                .with(finishedPaymentRoutingKey);
    }


}
