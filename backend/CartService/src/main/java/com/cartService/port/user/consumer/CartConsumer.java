package com.cartService.port.user.consumer;

import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.core.domain.service.interfaces.CartItemService;
import com.cartService.core.domain.service.interfaces.CartService;
import com.cartService.port.dto.ProductCartDTO;
import com.cartService.port.dto.mapper.ProductCartMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartConsumer.class);

    @Autowired
    private CartItemService cartItemService;

    @RabbitListener(queues = {"product_update"})
    public void updateProductInfoInCart(String message) throws CartItemNotFound {
        LOGGER.info("Received message -> {}", message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductCartDTO productCartDTO = objectMapper.readValue(message, ProductCartDTO.class);

            ProductCartMapper productCartMapper = new ProductCartMapper();
            CartItem updatedCartItem = productCartMapper.getCartItemFromProductCartDTO(productCartDTO);

            cartItemService.updateCartItem(updatedCartItem);

            LOGGER.info("Received message -> {}", message);

        } catch (Exception e) {
            LOGGER.error("Failed to handle product quantity change message", e);
        }
    }
}