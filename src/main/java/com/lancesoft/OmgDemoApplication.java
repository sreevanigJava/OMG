package com.lancesoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OmgDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmgDemoApplication.class, args);
		System.out.println("......................");
		System.out.println(">>>>>>>>>>>>>>>>>");
	}

}
