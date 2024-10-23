package com.example.product.init;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "couchbase")
public class CouchbaseHandler implements ApplicationRunner {

    private final ProductRepository productRepository;
    private List<String> buckets;

    public CouchbaseHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        productRepository.createBuckets(buckets);
        productRepository.bulkInsertAsync(getInitialProducts());
    }

    private List<Product> getInitialProducts() {
        List<Product> productList = new ArrayList<>();

        Product iphone = Product.builder()
                .id("iphone-1000-InsufficientStock")
                .name("iphone se 1")
                .isValid(true)
                .stockQuantity(0)
                .tax(BigDecimal.TEN)
                .discount(new BigDecimal(20))
                .price(new BigDecimal(1000))
                .build();
        productList.add(iphone);


        Product iphone2 = Product.builder()
                .id("apple-2000")
                .name("iphone 15")
                .isValid(true)
                .stockQuantity(100)
                .tax(new BigDecimal(20))
                .discount(new BigDecimal(40))
                .price(new BigDecimal(2000))
                .build();
        productList.add(iphone2);

        Product macbook = Product.builder()
                .id("macbook-10000")
                .name("macbook pro")
                .isValid(true)
                .stockQuantity(1000)
                .tax(new BigDecimal(2000))
                .discount(new BigDecimal(1000))
                .price(new BigDecimal(10000))
                .build();
        productList.add(macbook);

        Product nokia = Product.builder()
                .id("nokia-3310-notValid")
                .name("Nokia 3310")
                .isValid(false)
                .stockQuantity(10)
                .tax(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .price(new BigDecimal(100))
                .build();
        productList.add(nokia);
        return productList;
    }

}
