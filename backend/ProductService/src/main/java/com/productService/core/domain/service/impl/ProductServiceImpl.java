package com.productService.core.domain.service.impl;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductService;
import com.productService.core.domain.service.interfaces.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository){

        this.productRepository = productRepository;
    }

    public void createProduct (Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {

        productRepository.delete(product);
    }

    @Override
    public Product getProduct(int id) {
 // verzeichnis des services
        return null;
    }

    @Override
    public Product getProductById(UUID id) {

        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Product> getAllProducts() {

        return productRepository.findAll();
    }

}
