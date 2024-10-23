package com.example.order_service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@JsonDeserialize
public class Order {

    @Id
    private String id;
    private List<Product> products;
    private BigDecimal totalPrice;
    private String customerName;
    private String date;

    public Order() {
        this.id = UUID.randomUUID().toString();
    }

    public BigDecimal getTotalPrice() {
        return products.stream()
                .map(Product::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

