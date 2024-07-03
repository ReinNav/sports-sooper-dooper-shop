package com.cartService.core.domain.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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

    @ElementCollection
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "cart_item_id")
    @Column(name = "quantity")
    private Map<CartItem, Integer> cartItems = new HashMap<>();

    @Getter
    private float totalPrice;

    @Getter
    private int totalNumberOfItems;

    public void calculateTotalPriceAndItems() {
        updateTotalPrice();
        updateTotalNumberOfItems();
    }

    /**
     * Updates the total price of the cart based on the items and their quantities.
     */
    private void updateTotalPrice() {
        this.totalPrice = (float) cartItems.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }



    /**
     * Updates the total number of items in the cart.
     */
    private void updateTotalNumberOfItems() {
        this.totalNumberOfItems = cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }
}
