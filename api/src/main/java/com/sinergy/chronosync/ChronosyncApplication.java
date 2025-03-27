package com.sinergy.chronosync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
public class ChronosyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChronosyncApplication.class, args);
	}

}
