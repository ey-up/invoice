package com.example.product.exception;

public class StockCasMisMatchException extends RuntimeException {
    public StockCasMisMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}