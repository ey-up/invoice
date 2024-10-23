package com.example.product.configuration.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserRestTemplateConfig {

    @Value("${client.user-service.connect-timeout}")
    private int connectTimeout;

    @Value("${client.user-service.read-timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate userRestTemplate() {
        return buildRestTemplate(connectTimeout, readTimeout);
    }

    private RestTemplate buildRestTemplate(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}
