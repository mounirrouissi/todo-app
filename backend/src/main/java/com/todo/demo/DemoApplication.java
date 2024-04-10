package com.todo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Profile("aws")
	@EventListener(ApplicationReadyEvent.class)
	public void logWhenProfileBBIsActive() {
		System.out.println("Profile aws is active!");
	}
}
