package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.example")
public class DistributedJobQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedJobQueueApplication.class, args);
	}
}

