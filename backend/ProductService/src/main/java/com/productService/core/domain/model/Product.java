package com.productService.core.domain.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private float price;

    @Getter @Setter
    private String gender;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private Category category;

    @Getter @Setter
    private String size;

    @Getter @Setter
    private String colour;

    @Getter @Setter
    private int amount;

    @Getter @Setter
    private String imageLink;

}
