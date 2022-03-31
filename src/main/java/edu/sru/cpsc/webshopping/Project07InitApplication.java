package edu.sru.cpsc.webshopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class Project07InitApplication {

	public static void main(String[] args) {
		SpringApplication.run(Project07InitApplication.class, args);
		System.out.println("Running");
	}

}
