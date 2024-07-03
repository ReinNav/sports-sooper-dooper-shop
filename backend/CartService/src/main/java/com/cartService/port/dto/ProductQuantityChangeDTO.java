package com.cartService.port.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductQuantityChangeDTO {
    UUID productId;
    int difference;
}
