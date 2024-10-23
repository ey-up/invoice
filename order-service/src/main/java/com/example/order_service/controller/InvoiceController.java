package com.example.order_service.controller;

import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.InvoiceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final OrderRepository orderRepository;

    public InvoiceController(InvoiceService invoiceService, OrderRepository orderRepository) {
        this.invoiceService = invoiceService;
        this.orderRepository = orderRepository;
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> createInvoice(@PathVariable("orderId") String movieId) {
        Optional<Order> order = orderRepository.findById(movieId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(movieId + " order not found");
        }
        byte[] pdfBytes = invoiceService.createInvoicePdf(order.get());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}