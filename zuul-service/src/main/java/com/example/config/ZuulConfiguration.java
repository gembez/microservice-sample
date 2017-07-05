package com.example.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ZuulConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }
}