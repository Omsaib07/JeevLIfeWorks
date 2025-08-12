package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO used to reset the user's password.
 * Contains the reset token and the new password to be set.
 */
@Data
public class NewPasswordRequest {
	
	/**
     * Password reset token received by the user (usually via email).
     * Cannot be blank.
     */
	@NotBlank 
	private String token;
	
	/**
     * The new password the user wants to set.
     * Must be at least 6 characters and cannot be blank.
     */
    @NotBlank 
    @Size(min = 6) 
    private String newPassword;
}
