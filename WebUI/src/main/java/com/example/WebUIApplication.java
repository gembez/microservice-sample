package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.*;
import org.springframework.cloud.sleuth.sampler.*;
import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication
public class WebUIApplication {


	public static void main(String[] args) {
		SpringApplication.run(WebUIApplication.class, args);
	}


	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	};

}
