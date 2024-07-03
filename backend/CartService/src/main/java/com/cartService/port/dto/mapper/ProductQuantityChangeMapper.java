package com.cartService.port.dto.mapper;

import com.cartService.core.domain.model.CartItem;
import com.cartService.port.dto.ProductQuantityChangeDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ProductQuantityChangeMapper {

    public ProductQuantityChangeDTO getProductQuantityChangeDTO(CartItem item, int difference){
        ProductQuantityChangeDTO productQuantityChangeDTO = new ProductQuantityChangeDTO();
        productQuantityChangeDTO.setProductId(item.getProductId());
        productQuantityChangeDTO.setDifference(difference);
        return productQuantityChangeDTO;
    }
}
