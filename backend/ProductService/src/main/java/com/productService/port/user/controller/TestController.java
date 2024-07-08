package com.productService.port.user.controller;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductRepository;
import com.productService.port.user.dto.ProductCartDTO;
import com.productService.port.user.dto.mapper.ProductToProductCartDtoMapper;
import com.productService.port.user.producer.ProductProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TestController {

	@Autowired
    ProductProducer productProducer;

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Autowired
	ProductRepository productRepository;

	@GetMapping("/product-queue")
	public void triggerProductQueue(@RequestParam UUID productId) {
		try {
			Product product = productRepository.findById(productId).orElseThrow();
			productProducer.updateProductInfoInCart("product.product.updated", product);
		} catch (Exception e) {
			LOGGER.error("Failed to trigger product queue", e);
		}
	}

	@GetMapping("/product-update-queue")
	public void triggerProductUpdateQueue() {
		try {
			//updateProdcutProducer.sendMessage("update");
		} catch (Exception e) {
		}
	}


}
