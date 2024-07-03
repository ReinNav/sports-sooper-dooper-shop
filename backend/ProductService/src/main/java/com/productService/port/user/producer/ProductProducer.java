package com.productService.port.user.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productService.core.domain.model.Product;
import com.productService.port.user.dto.ProductCartDTO;
import com.productService.port.user.dto.mapper.ProductToProductCartDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("product")
    private String exchange;

    public ProductProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String routingKey, Object object){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageAsString = objectMapper.writeValueAsString(object);
            LOGGER.info(String.format("Message sent -> %s", messageAsString));
            rabbitTemplate.convertAndSend(exchange, routingKey, messageAsString);

        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize message object", e);
            throw new RuntimeException("Failed to serialize message object", e);
        }
    }

    public void updateProductInfoInCart(String routingKey, Product product) {
        ProductToProductCartDtoMapper productToProductCartDtoMapper = new ProductToProductCartDtoMapper();
        ProductCartDTO productCartDTO = productToProductCartDtoMapper.mapProductToProductCartDto(product);

        this.sendMessage(routingKey, productCartDTO);
    }
}