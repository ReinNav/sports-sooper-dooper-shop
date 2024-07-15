package com.cartService.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CART_ITEM")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID productId;

    @Setter
    private String name;

    @Setter
    private float price;

    @Setter
    private String gender;

    @Setter
    private String size;

    @Setter
    private String colour;

    @Setter
    private String imageLink;

    @Setter
    private int quantity;
}