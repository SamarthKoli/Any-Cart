package com.anycart.anycart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.anycart.anycart"})
public class AnycartApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnycartApplication.class, args);
	}

}
