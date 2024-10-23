package com.example.order_service.service;

import com.example.order_service.exception.InvoiceErrorException;
import com.example.order_service.model.Order;
import com.example.order_service.model.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    public byte[] createInvoicePdf(Order order) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 700);

                contentStream.showText("Invoice ID: " + order.getId());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Date: " + order.getDate());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Products:");
                contentStream.newLineAtOffset(0, -15);

                for (Product product : order.getProducts()) {
                    contentStream.showText("   Name: " + product.getName());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("   Price: " + product.getPrice());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("   Tax: " + product.getTax());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("   Discount: " + product.getDiscount());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("   Stock Quantity: " + product.getStockQuantity());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("   Quantity: " + product.getQuantity() + " units");
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Total Price: " + order.getTotalPrice());

                contentStream.endText();
            }
            document.save(byteArrayOutputStream);
        } catch (Exception e) {
            logger.error("InvoiceErrorException");
            throw new InvoiceErrorException(e.getMessage());
        }

        return byteArrayOutputStream.toByteArray();
    }
}
