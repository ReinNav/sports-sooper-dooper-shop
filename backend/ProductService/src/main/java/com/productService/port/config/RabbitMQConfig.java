package com.productService.port.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("product_update")
    private String productUpdateQueue;

    @Value("cart_update")
    private String cartUpdateQueue;

    @Value("product")
    private String productExchange;

    @Value("cart")
    private String cartExchange;

    @Value("product.product.updated")
    private String productUpdatedRoutingKey;

    @Value("cart.item.added")
    private String cartUpdatedRoutingKey;

    @Bean
    public Queue cartUpdateQueue(){
        return new Queue(cartUpdateQueue);
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
    public TopicExchange cartExchange(){
        return new TopicExchange(cartExchange);
    }


    @Bean
    public Binding cartBinding(){
        return BindingBuilder
                .bind(cartUpdateQueue())
                .to(cartExchange())
                .with(cartUpdatedRoutingKey);
    }

    @Bean
    public Binding productBinding(){
        return BindingBuilder
                .bind(productUpdateQueue())
                .to(productExchange())
                .with(productUpdatedRoutingKey);
    }

}