package com.webproject.pms.config;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.webproject.pms")
public class MvcConfig implements WebMvcConfigurer {

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
}
