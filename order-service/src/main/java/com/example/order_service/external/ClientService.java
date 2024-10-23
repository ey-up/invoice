package com.example.order_service.external;

import com.example.order_service.exception.StockDecrementException;
import com.example.order_service.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class ClientService {
    protected final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> T post(String url, Object requestBody, Class<T> responseType) {
        try {
            return restTemplate.postForObject(url, requestBody, responseType);
        } catch (HttpClientErrorException e) {
            logger.error("Client tarafında bir hata oluştu: {}{}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new StockDecrementException(e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            logger.error("Server tarafında bir hata oluştu: {}", e.getStatusCode());
            throw e;
        } catch (RestClientException e) {
            logger.error("Bir hata oluştu: {}", e.getMessage());
            throw e;
        }
    }
}
