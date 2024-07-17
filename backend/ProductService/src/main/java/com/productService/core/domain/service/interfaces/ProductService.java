package com.productService.core.domain.service.interfaces;

import com.productService.core.domain.model.Product;

import java.util.UUID;

public interface ProductService
{
    Product createProduct (Product product);

    void updateProduct (Product product);

    void deleteProduct (UUID id);

    Product getProduct(UUID id);

    Iterable<Product> getAllProducts();
}
