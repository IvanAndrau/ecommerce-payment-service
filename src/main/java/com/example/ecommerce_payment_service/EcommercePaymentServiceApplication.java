package com.example.ecommerce_payment_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommercePaymentServiceApplication {

	public static void main(String[] args) {
		// Load .env file
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // Optional: avoid crashing if .env is not found
				.load();

		// Optionally: set to system properties if Spring should access it
		System.setProperty("jwt.secret", dotenv.get("JWT_SECRET"));
		System.setProperty("stripe.api.key", dotenv.get("STRIPE_SECRET_KEY"));

		SpringApplication.run(EcommercePaymentServiceApplication.class, args);
	}

}
