package com.jeevlifeworks.Smart.Task.Manager.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeevlifeworks.Smart.Task.Manager.App.dto.LoginRequest;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.NewPasswordRequest;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.PasswordResetRequest;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.RegistrationRequest;
import com.jeevlifeworks.Smart.Task.Manager.App.security.JwtTokenUtil;
import com.jeevlifeworks.Smart.Task.Manager.App.service.AuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	@Autowired 
	private AuthenticationManager authenticationManager;

	@Autowired 
	private JwtTokenUtil jwtUtil;

	@Autowired 
	private AuthService authService;

	/**
	 * Registers a new user with the system.
	 * Validates input and delegates registration logic to AuthService.
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
		log.info("Received registration request for email: "+ registrationRequest.getEmail());
		authService.registerUser(registrationRequest);
		return ResponseEntity.ok("User registered successfully.  Please check your email to verify your account.");
	}

	/**
	 * Confirms the user's registration via email token.
	 * Token is verified in AuthService.
	 */
	@GetMapping("/register/confirm")
	public ResponseEntity<String> confirmRegistration(@RequestParam String token) {
		log.info("Received email confirmation request with token.");
		authService.confirmRegistration(token);
		return ResponseEntity.ok("Account verified successfully! You can now log in.");
	}

	/**
	 * Authenticates a user using email and password.
	 * If valid, generates and returns a JWT token for session.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Received login request for email: "+ loginRequest.getEmail());
		// Authenticate user credentials
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		// Set authentication context
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Generate JWT token
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwt = jwtUtil.generateToken(userDetails);

		log.info("User "+ loginRequest.getEmail() +" logged in successfully.");
		return ResponseEntity.ok(jwt);
	}

	/**
	 * Handles forgot password functionality.
	 * Sends password reset email if the email is registered.
	 */
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
		log.info("Received forgot password request for email: "+ passwordResetRequest.getEmail());
		authService.forgotPassword(passwordResetRequest.getEmail());
		return ResponseEntity.ok("If a user with that email exists, a password reset email has been sent.");
	}

	/**
	 * Resets the user's password using the reset token and new password.
	 */
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest) {
		log.info("Received password reset request.");
		authService.resetPassword(newPasswordRequest.getToken(), newPasswordRequest.getNewPassword());
		return ResponseEntity.ok("Password has been reset successfully.");
	}
}
