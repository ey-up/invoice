package com.example.product.service;

import com.example.product.dto.*;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductValidationSummaryResponse validateProducts(ProductValidationSummaryRequest request) {
        ProductValidationSummaryResponse response = new ProductValidationSummaryResponse();

        List<String> productIds = request.getProducts().stream().map(ProductValidationRequest::getId).collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        Map<String, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));

        List<ProductValidationResponse> results = request.getProducts().stream().
                map(productRequest -> validateSingleProduct(productRequest, productMap.get(productRequest.getId())))
                .collect(Collectors.toList());

        response.setResults(results);
        return response;
    }


    public void decrementStocks(UpdateStockRequest updateStockRequest) {
        this.productRepository.decrementStocks(updateStockRequest);
    }

    private ProductValidationResponse validateSingleProduct(ProductValidationRequest productRequest, Product product) {
        ProductValidationResponse productResponse = new ProductValidationResponse();
        productResponse.setId(productRequest.getId());

        if (isProductNotFound(product)) {
            productResponse.setValid(false);
            productResponse.setMessage("Product " + productRequest.getId() + " not found.");
        } else if (!product.isValid()) {
            productResponse.setValid(false);
            productResponse.setMessage("Product " + product.getName() + " is invalid");
        } else if (isInsufficientStock(product, productRequest.getQuantity())) {
            productResponse.setValid(false);
            productResponse.setMessage("Insufficient stock for product " + productRequest.getId() + ".");
        } else {
            productResponse.setValid(true);
            productResponse.setMessage("Product " + productRequest.getId() + " is valid and sufficient stock available.");
        }

        productResponse.setName(product.getName());
        productResponse.setQuantity(productRequest.getQuantity());
        productResponse.setPrice(product.getPrice());
        productResponse.setDiscount(product.getDiscount());
        productResponse.setTax(product.getTax());
        productResponse.setStockQuantity(product.getStockQuantity());
        return productResponse;
    }


    private boolean isProductNotFound(Product product) {
        return product == null;
    }

    private boolean isInsufficientStock(Product product, int requestedQuantity) {
        return product.getStockQuantity() < requestedQuantity;
    }


}
