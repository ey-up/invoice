package com.example.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductValidationSummaryRequest {
    private List<ProductValidationRequest> products;
}
