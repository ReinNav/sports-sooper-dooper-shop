package com.productService.core.domain.service.impl;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.exception.ProductNotFoundException;
import com.productService.core.domain.service.interfaces.ProductService;
import com.productService.core.domain.service.interfaces.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException(product.getId());
        }
    }

    @Override
    public void deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
