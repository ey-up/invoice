package com.example.order_service.controller;

import com.example.order_service.dto.request.OrderRequestDTO;
import com.example.order_service.dto.response.OrderDto;
import com.example.order_service.model.Order;
import com.example.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> order(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.placeOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.create(order));
    }

}
