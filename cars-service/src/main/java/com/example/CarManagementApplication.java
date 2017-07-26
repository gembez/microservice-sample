package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.sleuth.*;
import org.springframework.cloud.sleuth.sampler.*;
import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@SpringBootApplication
@EnableDiscoveryClient
public class CarManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarManagementApplication.class, args);
	}

	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	};



}