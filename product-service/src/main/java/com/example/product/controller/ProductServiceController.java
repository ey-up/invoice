package com.example.product.controller;

import com.example.product.dto.*;
import com.example.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductServiceController {

    private final ProductService productService;

    public ProductServiceController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ProductValidationSummaryResponse> validateProducts(@RequestBody ProductValidationSummaryRequest request) {
        ProductValidationSummaryResponse response = productService.validateProducts(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stock/decrement")
    public ResponseEntity<String> decrementStock(@RequestBody UpdateStockRequest updateStockRequest) {
        productService.decrementStocks(updateStockRequest);
        return ResponseEntity.ok("Stock successfully decremented for all products.");
    }


}
