package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.demo.aspect.LoggingAspect;
import com.example.demo.service.DemoService;

@SpringBootApplication
@Import(LoggingAspect.class)
public class DemoApplication implements CommandLineRunner{

	private final DemoService demoService;

	public DemoApplication(DemoService demoService) {
		this.demoService = demoService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		this.demoService.performTask();
	}
}
