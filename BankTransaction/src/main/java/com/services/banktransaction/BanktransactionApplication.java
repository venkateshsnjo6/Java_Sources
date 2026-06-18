package com.services.banktransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.isolve.banktransaction.*")
public class BanktransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanktransactionApplication.class, args);
	}

}
