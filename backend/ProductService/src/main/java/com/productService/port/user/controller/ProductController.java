package com.productService.port.user.controller;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product) {
        productService.createProduct(product);
    }

    @GetMapping("{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PutMapping(path = "/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        product.setId(id);
        productService.updateProduct(product);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }
}
