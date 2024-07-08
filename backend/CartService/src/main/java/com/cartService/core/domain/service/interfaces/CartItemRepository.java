package com.cartService.core.domain.service.interfaces;
import com.cartService.core.domain.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByProductId(UUID productId);
}
