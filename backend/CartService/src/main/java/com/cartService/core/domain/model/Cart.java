package com.cartService.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CART")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    @OrderColumn
    private List<CartItem> cartItems;

    @Getter
    private float totalPrice;

    @Getter
    private int totalNumberOfItems;

    public void calculateTotalPriceAndItems() {
        updateTotalPrice();
        updateTotalNumberOfItems();
    }

    private void updateTotalPrice() {
        this.totalPrice = (float) cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private void updateTotalNumberOfItems() {
        this.totalNumberOfItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}