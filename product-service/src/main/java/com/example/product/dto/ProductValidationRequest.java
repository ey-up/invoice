package com.example.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductValidationRequest {
    private String id;
    private int quantity;
}