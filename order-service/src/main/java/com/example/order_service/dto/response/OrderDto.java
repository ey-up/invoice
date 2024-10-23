package com.example.order_service.dto.response;

import com.example.order_service.model.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {
    private String id;
    private List<ProductDto> products;
    private BigDecimal totalPrice;
    private String customerName;
    private String date;

    public static OrderDto create(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCustomerName("Michael Scofield");
        orderDto.setDate(order.getDate());
        orderDto.setTotalPrice(order.getTotalPrice());

        orderDto.setProducts(order.getProducts().stream().map(ProductDto::create).toList());
        return orderDto;
    }
}
