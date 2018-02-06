package com.banmatrip.alert;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@MapperScan("com.banmatrip.alert.dao")
@EnableAutoConfiguration
public class AlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertApplication.class, args);
	}
}
