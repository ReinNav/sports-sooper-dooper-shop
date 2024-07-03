package com.productService.port.user.consumer;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductService;
import com.productService.port.user.dto.ProductQuantityChangeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = {"product"})
    public void consume(String message) {
        LOGGER.info(String.format("Received message -> %s", message));
        // Handle message appropriately
    }

    @RabbitListener(queues = {"cart_update"})
    public void handleProductQuantityChange(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductQuantityChangeDTO productQuantityChangeDTO = objectMapper.readValue(message, ProductQuantityChangeDTO.class);

            UUID productId = productQuantityChangeDTO.getProductId();
            int quantityChange = productQuantityChangeDTO.getDifference();

            Product product = productService.getProductById(productId);
            if (product != null) {
                int newAmount = product.getAmount() + quantityChange;
                product.setAmount(newAmount);
                productService.updateProduct(product);

                LOGGER.info("Updated product inventory for productId: {}, new amount: {}", productId, newAmount);
            } else {
                LOGGER.warn("Product not found for productId: {}", productId);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to handle product quantity change message", e);
        }
    }
}
