package com.productService.port.user.controller;

import com.productService.core.domain.model.Category;
import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductService;
import com.productService.core.domain.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0f);
        product.setGender("Unisex");
        product.setCategory(Category.OBERTEILE);
        product.setSize("M");
        product.setColour("Red");
        product.setAmount(10);
        product.setImageLink("http://example.com/image.jpg");
    }

    @Test
    void testCreateProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        productController.createProduct(product);
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void testGetProduct() {
        when(productService.getProduct(productId)).thenReturn(product);
        Product foundProduct = productController.getProduct(productId);
        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getId());
        verify(productService, times(1)).getProduct(productId);
    }

    @Test
    void testUpdateProduct() {
        doNothing().when(productService).updateProduct(any(Product.class));
        productController.updateProduct(productId, product);
        verify(productService, times(1)).updateProduct(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(productId);
        productController.deleteProduct(productId);
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void testGetProducts() {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));
        Iterable<Product> products = productController.getProducts();
        assertNotNull(products);
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProduct_NotFound() {
        UUID nonExistentProductId = UUID.randomUUID();
        when(productService.getProduct(nonExistentProductId)).thenThrow(new ProductNotFoundException(nonExistentProductId));
        assertThrows(ProductNotFoundException.class, () -> productController.getProduct(nonExistentProductId));
        verify(productService, times(1)).getProduct(nonExistentProductId);
    }
}
