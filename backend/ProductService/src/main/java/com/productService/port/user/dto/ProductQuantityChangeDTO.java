package com.productService.port.user.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityChangeDTO {
    private UUID productId;
    private int difference;
}
