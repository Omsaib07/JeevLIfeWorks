package com.jeevlifeworks.Smart.Task.Manager.App.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Role;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.RoleRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// Constructor injection for required dependencies
	public DatabaseInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * This method runs automatically when the application starts.
	 * It checks and initializes required roles and a default admin user in the database.
	 */
	@Override
	@Transactional
	public void run(String... args) {
		log.info("Checking for default roles in the database...");

		// Ensure all required roles exist
		Set<RoleEnum> requiredRoles = new HashSet<>(Arrays.asList(
				RoleEnum.ROLE_ADMIN,
				RoleEnum.ROLE_MANAGER,
				RoleEnum.ROLE_EMPLOYEE
				));

		// Create any missing roles in the database
		requiredRoles.forEach(roleEnum -> {
			if (!roleRepository.findByName(roleEnum).isPresent()) {
				log.info("Role "+ roleEnum.name() +" not found. Creating it now.");
				Role role = new Role();
				role.setName(roleEnum);
				roleRepository.save(role);
			} else {
				log.info("Role "+ roleEnum.name() +" already exists.");
			}
		});

		log.info("Database role initialization complete.");

		// Check and create a default admin user if one doesn't exist
		log.info("Checking for default admin user...");
		if (userRepository.findByEmail("admin@example.com").isEmpty()) {
			log.info("Default admin user not found. Creating it now.");
			// Create new admin user
			User admin = new User();
			admin.setUsername("Default Admin");
			admin.setEmail("admin@example.com");
			admin.setPassword(passwordEncoder.encode("password")); // Default password
			admin.setEnabled(true);

			// Assign ROLE_ADMIN to the default admin user
			Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
					.orElseThrow(() -> new IllegalStateException("Admin role not found during admin user creation."));
			admin.setRoles(Set.of(adminRole));

			// Save the admin user to the database
			userRepository.save(admin);
			log.info("Default admin user created successfully.");
			log.warn("Default admin credentials: email='admin@example.com', password='password'. Please change this password immediately in a production environment.");
		} else {
			log.info("Default admin user already exists.");
		}
	}
}
