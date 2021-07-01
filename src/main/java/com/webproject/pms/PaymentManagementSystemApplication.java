package com.webproject.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@SpringBootApplication(scanBasePackages = "com.webproject.pms")
public class PaymentManagementSystemApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentManagementSystemApplication.class, args);
	}
}
