package com.productService.core.domain.service.interfaces;

import com.productService.core.domain.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {

}


