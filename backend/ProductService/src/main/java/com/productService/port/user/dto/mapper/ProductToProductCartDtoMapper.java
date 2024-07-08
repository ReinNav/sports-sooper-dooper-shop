package com.productService.port.user.dto.mapper;

import com.productService.core.domain.model.Product;
import com.productService.port.user.dto.ProductCartDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductCartDtoMapper {

    public ProductCartDTO mapProductToProductCartDto(Product product){
        ProductCartDTO productCartDTO = new ProductCartDTO();
        productCartDTO.setProductId(product.getId());
        productCartDTO.setName(product.getName());
        productCartDTO.setPrice(product.getPrice());
        productCartDTO.setGender(product.getGender());
        productCartDTO.setSize(product.getSize());
        productCartDTO.setColour(product.getColour());
        return productCartDTO;
    }
}
