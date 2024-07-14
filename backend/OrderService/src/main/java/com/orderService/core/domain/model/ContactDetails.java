package com.orderService.core.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails {

    @Getter
    private String contactPhone;

    @Getter
    private String contactEmail;
}
