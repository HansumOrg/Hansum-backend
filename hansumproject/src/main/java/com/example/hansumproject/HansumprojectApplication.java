package com.example.hansumproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//OAuth 의존성
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HansumprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(HansumprojectApplication.class, args);
	}

}
