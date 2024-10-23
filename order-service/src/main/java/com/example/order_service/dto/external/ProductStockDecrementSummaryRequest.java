package com.example.order_service.dto.external;

import com.example.order_service.dto.request.OrderProductDTO;
import com.example.order_service.dto.request.OrderRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductStockDecrementSummaryRequest {
    private List<ProductRequest> products;

    public static ProductStockDecrementSummaryRequest convert(OrderRequestDTO orderRequestDTO) {
        ProductStockDecrementSummaryRequest productValidationRequest = new ProductStockDecrementSummaryRequest();
        List<ProductRequest> productRequests = new ArrayList<>();
        for (OrderProductDTO orderProduct : orderRequestDTO.getProducts()) {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setQuantity(orderProduct.getQuantity());
            productRequest.setId(orderProduct.getId());
            productRequests.add(productRequest);
        }
        productValidationRequest.setProducts(productRequests);
        return productValidationRequest;
    }
}
