package com.paymentService.port.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("payment_finished")
    private String paymentFinishedQueue;

    @Value("payment")
    private String paymentExchange;

    @Value("payment.payment.finished")
    private String finishedPaymentRoutingKey;

    @Bean
    public Queue paymentFinishedQueue(){
            return new Queue(paymentFinishedQueue);
    }

    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(paymentExchange);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(paymentFinishedQueue())
                .to(paymentExchange())
                .with(finishedPaymentRoutingKey);
    }

}
