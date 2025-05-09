package com.crack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrackApplication.class, args);
	}

}
