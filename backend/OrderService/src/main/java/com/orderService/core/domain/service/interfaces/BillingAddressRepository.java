package com.orderService.core.domain.service.interfaces;

import com.orderService.core.domain.model.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
}
