package com.productService.port.user.controller;

import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductService;
import com.productService.port.user.exception.ProductNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/product")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void create(@RequestBody Product product) {
        productService.createProduct(product);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    @PutMapping(path = "/product")
    public @ResponseBody String update() {

        return null;
    }

    @DeleteMapping(path = "/product")
    public @ResponseBody String delete() {

        return null;
    }

    @GetMapping("/products")
    public @ResponseBody Iterable<Product> getProducts() {

        return productService.getAllProducts();
    }

}
