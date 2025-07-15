package com.user;

//Import Spring Boot's core class to launch the application
import org.springframework.boot.SpringApplication;
//Import annotation to enable auto-configuration, component scanning, and configuration
import org.springframework.boot.autoconfigure.SpringBootApplication;

//This annotation marks the class as the main Spring Boot application class.
//It combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
@SpringBootApplication
public class UserServiceApplication {

	// The main method acts as the entry point for the Java application.
	public static void main(String[] args) {
		// This static method starts the Spring Boot application by:
		// - Bootstrapping the Spring context
		// - Performing classpath scanning
		// - Starting the embedded server (like Tomcat)
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
