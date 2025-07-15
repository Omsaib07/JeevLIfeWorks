package com.ems;
// Importing the necessary Spring Boot classes to bootstrap the application
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation marks this class as the entry point for Spring Boot application.
// It combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
@SpringBootApplication
public class EmployeeServiceApplication {

	// The main method serves as the entry point for the Java application.
	public static void main(String[] args) {
		// This line starts the Spring Boot application by invoking the run method.
		// It bootstraps the Spring context and starts the embedded server (like Tomcat).
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
