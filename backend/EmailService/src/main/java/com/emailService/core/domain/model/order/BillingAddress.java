package com.emailService.core.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BillingAddress {

    private UUID addressId;
    private String userId; // for getting saved addresses
    private String firstName;
    private String lastName;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
}
