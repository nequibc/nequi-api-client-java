package com.nequi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class}
)
public class NequiApiWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(NequiApiWebappApplication.class, args);
	}

}
