package com.cartService.port.dto;

import lombok.Getter;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductCartDTO {

     @Getter
     UUID productId;

     @Getter
     String name;

     @Getter
     float price;
     @Getter
     String gender;

     @Getter
     String size;
     @Getter
     String colour;
}