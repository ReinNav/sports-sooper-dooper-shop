package com.orderService.core.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID addressId;

    @Getter
    private String userId; // for getting saved addresses

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    private String street;

    @Getter
    private String houseNumber;

    @Getter
    private String city;

    @Getter
    private String postalCode;

    @Getter
    private String country;
}
