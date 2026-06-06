package com.ecommerce.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbEcomApplication {
	public static void main(String[] args) {
        System.out.println("****START APPLICATION****");
        SpringApplication.run(SbEcomApplication.class, args);
        System.out.println("****END APPLICATION****");
	}
}
