package com.jeevlifeworks.Smart.Task.Manager.App.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.PasswordResetToken;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Role;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.VerificationToken;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.RegistrationRequest;
import com.jeevlifeworks.Smart.Task.Manager.App.exception.EmailAlreadyExistsException;
import com.jeevlifeworks.Smart.Task.Manager.App.exception.InvalidTokenException;
import com.jeevlifeworks.Smart.Task.Manager.App.exception.ResourceNotFoundException;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.PasswordResetTokenRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.RoleRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.UserRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.VerificationTokenRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class that handles authentication-related business logic
 * such as user registration, email verification, password reset.
 */
@Service
@Slf4j
public class AuthService {

	@Autowired 
	private UserRepository userRepository;

	@Autowired 
	private RoleRepository roleRepository;

	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Autowired 
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired 
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired 
	private EmailService emailService;

	/**
	 * Registers a new user after validating that email is unique.
	 * Encodes password, assigns default ROLE_EMPLOYEE, creates verification token,
	 * sends verification email, and saves user.
	 * 
	 * Accepts paramater registrationRequest DTO containing username, email, and password
	 * return saved User entity
	 * throws EmailAlreadyExistsException if email is already registered
	 */
	@Transactional
	public User registerUser(RegistrationRequest registrationRequest) {
		log.info("Attempting to register new user with email: "+ registrationRequest.getEmail());
		// Check if email already exists
		if (userRepository.existsByEmail(registrationRequest.getEmail())) {
			log.error("Registration failed: Email "+ registrationRequest.getEmail() +" is already in use.");
			throw new EmailAlreadyExistsException("Email is already in use!");
		}

		// Create new user entity and set basic info
		User user = new User();
		user.setUsername(registrationRequest.getUsername());
		user.setEmail(registrationRequest.getEmail());
		// Encode password before saving
		user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

		// By default, new users are employees
		Set<Role> roles = new HashSet<>();
		Role employeeRole = roleRepository.findByName(RoleEnum.ROLE_EMPLOYEE)
				.orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
		roles.add(employeeRole);
		user.setRoles(roles);

		// Save user to database
		User savedUser = userRepository.save(user);
		log.info("User registered successfully. User ID: "+ savedUser.getId());

		// Create and save verification token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(savedUser);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
		verificationTokenRepository.save(verificationToken);
		log.debug("Verification token created for user "+ savedUser.getEmail() +": "+token);

		// Send verification email
		emailService.sendVerificationEmail(savedUser.getEmail(), token);
		log.info("Verification email sent to: "+ savedUser.getEmail());

		return savedUser;
	}

	/**
	 * Confirms user registration by validating verification token.
	 * Enables the user if token is valid and deletes the token.
	 * 
	 * Accepts parameter token verification token string
	 * throws InvalidTokenException if token is invalid or expired
	 */
	@Transactional
	public void confirmRegistration(String token) {
		log.info("Attempting to confirm registration with token: {}", token);
		Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

		if (optionalToken.isEmpty()) {
			log.error("Confirmation failed: Invalid token used.");
			throw new InvalidTokenException("Invalid or expired token.");
		}

		VerificationToken verificationToken = optionalToken.get();
		// Check if token expired
		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			log.error("Confirmation failed: Token has expired.");
			throw new InvalidTokenException("Token has expired. Please request a new one.");
		}

		// Enable user account
		User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);
		// Delete verification token after successful confirmation
		verificationTokenRepository.delete(verificationToken);
		log.info("User "+ user.getEmail() +" account confirmed and enabled.");
	}

	/**
	 * Initiates the forgot password process by creating a reset token
	 * and sending a reset email.
	 * 
	 * Accepts parameter email the user's email address
	 * throws ResourceNotFoundException if no user found with given email
	 */
	@Transactional
	public void forgotPassword(String email) {
		log.info("Password reset requested for email: "+ email);
		// Find user by email or throw exception if not found
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					log.warn("Password reset requested for non-existent email: "+ email);
					return new ResourceNotFoundException("User with this email not found.");
				});

		// Generate reset token with 30-minute expiry
		String token = UUID.randomUUID().toString();
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30-minute expiry
		passwordResetTokenRepository.save(resetToken);
		log.debug("Password reset token created for user "+ user.getEmail() +": "+ token);

		// Send password reset email with token link
		emailService.sendPasswordResetEmail(user.getEmail(), token);
		log.info("Password reset email sent to: "+ user.getEmail());
	}

	/**
	 * Resets the user's password using a valid reset token.
	 * Updates the user's password with encoded new password and deletes token.
	 * 
	 * Accepts parameter token the password reset token string
	 * Accepts parameter newPassword the new password to set
	 * throws InvalidTokenException if token is invalid or expired
	 */
	@Transactional
	public void resetPassword(String token, String newPassword) {
		log.info("Attempting to reset password with token.");
		Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
		if (optionalToken.isEmpty()) {
			log.error("Password reset failed: Invalid token used.");
			throw new InvalidTokenException("Invalid or expired token.");
		}

		PasswordResetToken resetToken = optionalToken.get();

		// Check token expiry
		if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			log.error("Password reset failed: Token has expired.");
			throw new InvalidTokenException("Token has expired.");
		}

		// Update user password with encoded new password
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		// Delete token after successful reset
		passwordResetTokenRepository.delete(resetToken);
		log.info("Password successfully reset for user: "+ user.getEmail());
	}
}
