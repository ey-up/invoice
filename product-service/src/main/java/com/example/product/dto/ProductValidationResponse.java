package com.example.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductValidationResponse {
    private String id;
    private boolean isValid;
    private String message;
    private String name;
    private BigDecimal tax;
    private BigDecimal discount;
    private int stockQuantity;
    private int quantity;
    private BigDecimal price;
}