package com.cartService.port.dto.mapper;

import com.cartService.core.domain.model.CartItem;
import com.cartService.port.dto.ProductCartDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductCartMapper {

    public CartItem getCartItemFromProductCartDTO(ProductCartDTO productCartDTO){
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productCartDTO.getProductId());
        cartItem.setPrice(productCartDTO.getPrice());
        cartItem.setName(productCartDTO.getName());
        cartItem.setColour(productCartDTO.getColour());
        cartItem.setSize(productCartDTO.getSize());
        cartItem.setGender(productCartDTO.getGender());
        return cartItem;
    }
}
