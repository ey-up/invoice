package com.example.order_service.external;

import com.example.order_service.dto.external.ProductStockDecrementSummaryRequest;
import com.example.order_service.dto.external.ProductValidationRequest;
import com.example.order_service.dto.external.ProductValidationSummaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClientService extends ClientService {

    @Value("${client.product-service.base-url}")
    private String productServiceBaseUrl;

    public ProductClientService(RestTemplate productRestTemplate) {
        super(productRestTemplate);
    }

    public ProductValidationSummaryResponse validate(ProductValidationRequest productValidationRequest) {
        String url = productServiceBaseUrl + "/products/validate";

        return post(url, productValidationRequest, ProductValidationSummaryResponse.class);
    }

    public String decrementStock(ProductStockDecrementSummaryRequest productStockDecrementSummaryRequest) {
        String url = productServiceBaseUrl + "/products/stock/decrement";
        return post(url, productStockDecrementSummaryRequest, String.class);
    }

}