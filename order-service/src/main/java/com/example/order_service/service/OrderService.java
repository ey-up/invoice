package com.example.order_service.service;

import com.example.order_service.dto.external.ProductStockDecrementSummaryRequest;
import com.example.order_service.dto.external.ProductValidationRequest;
import com.example.order_service.dto.external.ProductValidationSummaryResponse;
import com.example.order_service.dto.request.OrderRequestDTO;
import com.example.order_service.exception.ProductInvalidException;
import com.example.order_service.external.ProductClientService;
import com.example.order_service.model.Order;
import com.example.order_service.model.Product;
import com.example.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final ProductClientService productClientService;
    private final OrderRepository orderRepository;

    public OrderService(ProductClientService productClientService, OrderRepository orderRepository) {
        this.productClientService = productClientService;
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(OrderRequestDTO orderRequestDTO) {
        ProductValidationSummaryResponse validationSummary = validateProducts(orderRequestDTO);
        decrementProductStock(orderRequestDTO);
        Order order = createOrder(orderRequestDTO, validationSummary);
        return orderRepository.save(order);
    }

    private ProductValidationSummaryResponse validateProducts(OrderRequestDTO orderRequestDTO) {
        ProductValidationSummaryResponse validationSummary = productClientService.validate(ProductValidationRequest.convert(orderRequestDTO));
        validationSummary.getResults().stream()
                .filter(result -> !result.isValid())
                .findFirst()
                .ifPresent(result -> {
                    throw new ProductInvalidException(result.getMessage());
                });
        return validationSummary;
    }

    private void decrementProductStock(OrderRequestDTO orderRequestDTO) {
        productClientService.decrementStock(ProductStockDecrementSummaryRequest.convert(orderRequestDTO));
    }

    private Order createOrder(OrderRequestDTO orderRequestDTO, ProductValidationSummaryResponse validationSummary) {
        Order order = new Order();
        order.setDate(LocalDateTime.now().toString());
        order.setProducts(validationSummary.getResults().stream()
                .map(Product::create)
                .toList());

        order.setTotalPrice(order.getTotalPrice());

        return order;
    }

//    public void placeOrder2(OrderRequestDTO orderRequestDTO) {
//        ProductValidationSummaryResponse validate = productClientService.validate(ProductValidationRequest.convert(orderRequestDTO));
//        for (ProductValidationResponse result : validate.getResults()) {
//            if (!result.isValid()) {
//                throw new ProductInvalidException(result.getMessage());
//            }
//        }
//
//        productClientService.decrementStock(ProductStockDecrementSummaryRequest.convert(orderRequestDTO));
//        Order order = new Order();
//        order.setDate(String.valueOf(new Date()));
//        order.setProducts(validate.getResults().stream().map(Product::create).toList());
//        order.setTotalPrice(order.getTotalPrice());
//
//        orderRepository.save(order);




        // todo kodu temizleyeceğiz

        // todo read me
//    }
}
