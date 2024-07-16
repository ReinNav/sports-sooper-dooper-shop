package com.productService.core.domain.service.impl;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.exception.ProductNotFoundException;
import com.productService.core.domain.service.interfaces.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void createProduct() {
        Product product = new Product();
        productService.createProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        when(productRepository.existsById(product.getId())).thenReturn(true);
        productService.updateProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_NotFound() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        when(productRepository.existsById(product.getId())).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    void deleteProduct() {
        UUID id = UUID.randomUUID();
        when(productRepository.existsById(id)).thenReturn(true);
        productService.deleteProduct(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteProduct_NotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.existsById(id)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(id));
    }

    @Test
    void getProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProduct(id);
        assertEquals(product, foundProduct);
    }

    @Test
    void getProduct_NotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(id));
    }

    @Test
    void getAllProducts() {
        productService.getAllProducts();
        verify(productRepository, times(1)).findAll();
    }
}
