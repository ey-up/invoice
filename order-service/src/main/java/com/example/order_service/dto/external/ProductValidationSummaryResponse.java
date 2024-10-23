package com.example.order_service.dto.external;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductValidationSummaryResponse {
    private List<ProductValidationResponse> results;
}
