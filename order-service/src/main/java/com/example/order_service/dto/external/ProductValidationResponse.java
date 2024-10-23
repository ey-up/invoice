package com.example.order_service.dto.external;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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