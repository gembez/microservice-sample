package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.*;
import org.springframework.context.annotation.*;
import org.springframework.cloud.sleuth.sampler.*;
import org.springframework.web.client.RestTemplate;

//@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class AccountingApplication {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Sampler defaultSampler() {
        return new AlwaysSampler();
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }

}
