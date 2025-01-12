package com.infy.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productName"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the product",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    private String description;
    private String country;

}
