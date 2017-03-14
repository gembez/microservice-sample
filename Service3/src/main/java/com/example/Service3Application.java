package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.*;
import org.springframework.context.annotation.*;
import org.springframework.cloud.sleuth.sampler.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Service3Application {


	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	};


	public static void main(String[] args) {
		SpringApplication.run(Service3Application.class, args);
	}


}
