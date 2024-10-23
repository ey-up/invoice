package com.example.order_service.dto.response;

import com.example.order_service.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private BigDecimal tax;
    private BigDecimal discount;
    private int stockQuantity;
    private BigDecimal price;
    private int quantity;


    public static ProductDto create(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setTax(product.getTax());
        productDto.setDiscount(product.getDiscount());
        productDto.setStockQuantity(product.getStockQuantity());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        return productDto;
    }
}
