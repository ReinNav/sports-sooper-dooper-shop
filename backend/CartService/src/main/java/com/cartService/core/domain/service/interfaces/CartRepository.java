package com.cartService.core.domain.service.interfaces;

import com.cartService.core.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(UUID userId);
}