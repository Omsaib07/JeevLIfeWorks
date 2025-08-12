package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for initiating a password reset request.
 * Captures the user's email address, which is required
 * to send the reset link/token.
 */
@Data
public class PasswordResetRequest {
	
	/**
     * Email of the user requesting a password reset.
     * Must be a valid email and cannot be blank.
     */	
	@NotBlank 
	@Email 
	private String email;
}
