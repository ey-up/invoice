package com.example.order_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.order_service.dto.external.ProductValidationResponse;
import com.example.order_service.dto.external.ProductValidationSummaryResponse;
import com.example.order_service.dto.request.OrderProductDTO;
import com.example.order_service.dto.request.OrderRequestDTO;
import com.example.order_service.exception.ProductInvalidException;
import com.example.order_service.external.ProductClientService;
import com.example.order_service.model.Order;
import com.example.order_service.model.Product;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductClientService productClientService;

    @Mock
    private OrderRepository orderRepository;

    private OrderRequestDTO orderRequestDTO;
    private ProductValidationSummaryResponse validationSummary;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setProducts(Arrays.asList(
                new OrderProductDTO("nokia-3310", 2),
                new OrderProductDTO("iphone-15", 5)
        ));

        validationSummary = new ProductValidationSummaryResponse();
        validationSummary.setResults(Arrays.asList(
                ProductValidationResponse.builder()
                        .id("nokia-3310")
                        .isValid(true)
                        .name("Nokia 3310")
                        .tax(BigDecimal.TEN)
                        .discount(BigDecimal.TEN)
                        .stockQuantity(100)
                        .quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .build(),
                ProductValidationResponse.builder()
                        .id("iphone-15")
                        .isValid(true)
                        .name("iPhone 15")
                        .tax(BigDecimal.valueOf(20))
                        .discount(BigDecimal.valueOf(40))
                        .stockQuantity(70)
                        .quantity(2)
                        .price(BigDecimal.valueOf(2000))
                        .build()

        ));
    }

    @Test
    public void testPlaceOrder_success() {
        // given
        when(productClientService.validate(any())).thenReturn(validationSummary);
        when(productClientService.decrementStock(any())).thenReturn(null);

        Order savedOrder = new Order();
        List<Product> products = createOrder();
        savedOrder.setProducts(products);
        savedOrder.setId("123");
        savedOrder.setDate(LocalDateTime.now().toString());
        savedOrder.setTotalPrice(BigDecimal.valueOf(10100));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // when
        Order order = orderService.placeOrder(orderRequestDTO);

        // then
        assertNotNull(order);
        assertEquals("123", order.getId());
        assertEquals(10100, order.getTotalPrice().doubleValue());
        assertEquals(orderRequestDTO.getProducts().size(), order.getProducts().size());

        verify(productClientService).validate(any());
        verify(productClientService).decrementStock(any());
        verify(orderRepository).save(any());
    }



    @Test
    public void testPlaceOrder_productInvalidException() {
        // given
        validationSummary.getResults().get(1).setValid(false);
        validationSummary.getResults().get(1).setMessage("Ürün geçersiz!");

        when(productClientService.validate(any())).thenReturn(validationSummary);

        // when
        Exception exception = assertThrows(ProductInvalidException.class, () -> {
            orderService.placeOrder(orderRequestDTO);
        });

        // then
        assertEquals("Ürün geçersiz!", exception.getMessage());

        verify(productClientService).validate(any());
        verify(productClientService, never()).decrementStock(any());
        verify(orderRepository, never()).save(any());
    }

    private static List<Product> createOrder() {
        return List.of(Product.builder()
                        .id("nokia-3310")
                        .name("Nokia 3310")
                        .tax(BigDecimal.TEN)
                        .discount(BigDecimal.TEN)
                        .stockQuantity(100)
                        .quantity(2)
                        .price(BigDecimal.valueOf(100))
                        .build(),
                Product.builder()
                        .id("iphone-15")
                        .name("iPhone 15")
                        .tax(BigDecimal.valueOf(20))
                        .discount(BigDecimal.valueOf(40))
                        .stockQuantity(70)
                        .quantity(5)
                        .price(BigDecimal.valueOf(2000))
                        .build());
    }
}