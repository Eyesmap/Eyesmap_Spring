package com.spring.eyesmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EyesmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(EyesmapApplication.class, args);
	}

}
