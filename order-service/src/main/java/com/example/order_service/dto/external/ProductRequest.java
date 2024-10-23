package com.example.order_service.dto.external;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String id;
    private int quantity;
}
