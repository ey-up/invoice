package com.example.order_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductRestTemplateConfig {

    @Value("${client.product-service.connect-timeout}")
    private int connectTimeout;

    @Value("${client.product-service.read-timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate productRestTemplate() {
        return buildRestTemplate(connectTimeout, readTimeout);
    }

    private RestTemplate buildRestTemplate(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}
