package com.example.order_service.model;

import com.example.order_service.dto.external.ProductValidationResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@Data
@Builder
@JsonDeserialize
@NoArgsConstructor
public class Product {

    private String id;
    private String name;
    private BigDecimal tax;
    private BigDecimal discount;
    private int stockQuantity;
    private BigDecimal price;
    private int quantity;


    public static Product create(ProductValidationResponse productValidationResponse) {
        if (Objects.isNull(productValidationResponse)) return null;

        Product product = new Product();
        product.setId(productValidationResponse.getId());
        product.setPrice(productValidationResponse.getPrice());
        product.setStockQuantity(productValidationResponse.getStockQuantity());
        product.setDiscount(productValidationResponse.getDiscount());
        product.setTax(productValidationResponse.getTax());
        product.setName(productValidationResponse.getName());
        product.setQuantity(productValidationResponse.getQuantity());
        return product;
    }

    public BigDecimal calculatePrice() {
        BigDecimal discountedPrice = price.subtract(discount);
        return discountedPrice.add(tax).multiply(BigDecimal.valueOf(quantity));

    }
}
