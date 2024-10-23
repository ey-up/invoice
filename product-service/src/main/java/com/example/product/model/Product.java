package com.example.product.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@JsonDeserialize
public class Product {

    @Id
    private String id;
    private String name;
    private BigDecimal tax;
    private BigDecimal discount;
    private int stockQuantity;
    private boolean isValid;
    private BigDecimal price;

    public Product() {
        this.id = UUID.randomUUID().toString();
    }


}
