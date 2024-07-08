package com.productService.core.domain.service.interfaces;

import com.productService.core.domain.model.Product;

import java.util.UUID;

public interface ProductService
{
    void createProduct (Product product);

    void updateProduct (Product product);

    void deleteProduct (Product product);

    Product getProduct(int id);

    Iterable<Product> getAllProducts();

    Product getProductById(UUID id);
}
