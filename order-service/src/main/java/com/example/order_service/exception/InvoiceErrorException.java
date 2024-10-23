package com.example.order_service.exception;

public class InvoiceErrorException extends RuntimeException{
    public InvoiceErrorException(String message) {
        super(message);
    }
}
