package com.orderService.port.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDTO {
    private UUID addressId;
    private String firstName;
    private String lastName;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
}
