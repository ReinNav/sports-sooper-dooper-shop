package com.orderService.core.domain.service.interfaces;

import com.orderService.core.domain.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, UUID> {
}
