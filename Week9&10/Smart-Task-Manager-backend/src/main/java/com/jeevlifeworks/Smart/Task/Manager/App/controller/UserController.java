package com.jeevlifeworks.Smart.Task.Manager.App.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Endpoint to retrieve a list of all users in the system.
	 * Accessible only by users with the 'ADMIN' role.
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("Received request to get all users.");
		return ResponseEntity.ok(userService.getAllUsers());
	}

	/**
	 * Endpoint to change the role of a specific user.
	 * Accessible only by users with the 'ADMIN' role.
	 *
	 * Accepts parameter userId ID of the user whose role is to be changed
	 * Accepts parameter role, New role to be assigned (e.g., ROLE_MANAGER, ROLE_EMPLOYEE)
	 * return Updated user object with the new role
	 */
	@PutMapping("/{userId}/role")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> changeUserRole(@PathVariable Long userId, @RequestParam String role) {
		log.info("Received request to change role for user ID "+ userId +" to "+ role);
		User updatedUser = userService.changeUserRole(userId, role);
		return ResponseEntity.ok(updatedUser);
	}

	/**
	 * Endpoint to delete a user from the system.
	 * Accessible only by users with the 'ADMIN' role.
	 *
	 * Accepts parameter userId ID of the user to be deleted
	 * return HTTP 204 No Content on successful deletion
	 */
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		log.info("Received request to delete user ID "+ userId);
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
}
