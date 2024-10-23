package com.example.order_service.dto.external;

import com.example.order_service.dto.request.OrderProductDTO;
import com.example.order_service.dto.request.OrderRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductValidationRequest {
    private List<ProductRequest> products;


    public static ProductValidationRequest convert(OrderRequestDTO requestDto) {
        ProductValidationRequest productValidationRequest = new ProductValidationRequest();
        List<ProductRequest> productRequests = new ArrayList<>();
        for (OrderProductDTO orderProduct : requestDto.getProducts()) {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setQuantity(orderProduct.getQuantity());
            productRequest.setId(orderProduct.getId());
            productRequests.add(productRequest);
        }
        productValidationRequest.setProducts(productRequests);
        return productValidationRequest;
    }
}
