package com.cartService.port.user.producer;

import com.cartService.core.domain.model.CartItem;
import com.cartService.port.dto.ProductQuantityChangeDTO;
import com.cartService.port.dto.mapper.ProductQuantityChangeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CartProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductQuantityChangeMapper productQuantityChangeMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("cart")
    private String exchange;

    public CartProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object messageObject, String routingKey) {
        try {
            String message = objectMapper.writeValueAsString(messageObject);
            LOGGER.info("Sending message: {}", message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize message object", e);
            throw new RuntimeException("Failed to serialize message object", e);
        }
    }

    public void changeProductAmount(CartItem item, int difference) {
        ProductQuantityChangeDTO productChangeDTO = productQuantityChangeMapper.getProductQuantityChangeDTO(item, difference);
        sendMessage(productChangeDTO, "cart.item.added");
    }
}
