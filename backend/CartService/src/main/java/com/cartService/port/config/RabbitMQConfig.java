package com.cartService.port.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("product_update")
    private String productUpdateQueue;

    @Value("cart_update")
    private String cartUpdateQueue;

    @Value("payment_finished")
    private String paymentFinishedQueue;

    @Value("cart")
    private String cartExchange;

    @Value("product")
    private String productExchange;

    @Value("cart.item.added")
    private String cartItemAddedRoutingKey;

    @Value("product.product.updated")
    private String productUpdatedRoutingKey;

    @Bean
    public Queue cartUpdateQueue(){
        return new Queue(cartUpdateQueue);
    }

    @Bean
    public TopicExchange cartExchange(){
        return new TopicExchange(cartExchange);
    }

    @Bean
    public Queue productUpdateQueue(){
        return new Queue(productUpdateQueue);
    }

    @Bean
    public TopicExchange productExchange(){
        return new TopicExchange(productExchange);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(cartUpdateQueue())
                .to(cartExchange())
                .with(cartItemAddedRoutingKey);
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder
                .bind(productUpdateQueue())
                .to(productExchange())
                .with(productUpdatedRoutingKey);
    }
}
