package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import java.util.Set;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing user information
 * to be sent to the client or received from client when needed.
 * This class excludes sensitive information like passwords.
 */
@Data
public class UserDto {

	// Unique user identifier
	private Long id;
	// User's login name
	private String username;
	// User's email address
	private String email;
	
	/**
	 * Set of role names assigned to the user (e.g. ROLE_ADMIN, ROLE_EMPLOYEE).
	 * Represented as strings for easier consumption on the frontend.
	 */
	private Set<String> roles;
}
