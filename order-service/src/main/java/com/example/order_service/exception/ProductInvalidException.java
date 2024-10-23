package com.example.order_service.exception;

public class ProductInvalidException extends RuntimeException{
    public ProductInvalidException(String message) {
        super(message);
    }
}
