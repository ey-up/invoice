package com.example.order_service.init;

import com.example.order_service.model.Order;
import com.example.order_service.model.Product;
import com.example.order_service.repository.OrderRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "couchbase")
public class CouchbaseHandler implements ApplicationRunner {

    private final OrderRepository orderRepository;
    private List<String> buckets;

    public CouchbaseHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

        orderRepository.save(createOrder());
    }

    private Order createOrder() {
        List<Product> products = List.of(Product.builder()
                        .id("macbook-10000")
                        .name("macbook pro")
                        .tax(new BigDecimal(2000))
                        .discount(new BigDecimal(1000))
                        .stockQuantity(1000)
                        .quantity(5)
                        .price(BigDecimal.valueOf(10000))
                        .build(),
                Product.builder()
                        .id("apple-2000")
                        .name("iphone 15")
                        .tax(BigDecimal.valueOf(20))
                        .discount(BigDecimal.valueOf(40))
                        .stockQuantity(100)
                        .quantity(5)
                        .price(BigDecimal.valueOf(2000))
                        .build());

        Order order = new Order();
        order.setProducts(products);
        order.setId("3edfb7d8-253f-4159-b008-8358ed1c9596");
        order.setTotalPrice(new BigDecimal(64900));
        return order;
    }


}
