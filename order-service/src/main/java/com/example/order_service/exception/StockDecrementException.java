package com.example.order_service.exception;

public class StockDecrementException extends RuntimeException{
    public StockDecrementException(String message) {
        super(message);
    }
}