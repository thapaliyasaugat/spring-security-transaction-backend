package com.securitytest.securitytest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class SecuritytestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuritytestApplication.class, args);
	}
@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
}
}
