package com.tinFInale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SpringBootJwtApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SpringBootJwtApplication.class);
		springApplication.setDefaultProperties(Collections.singletonMap("server.port","9090"));
		springApplication.run(args);
	}
}
