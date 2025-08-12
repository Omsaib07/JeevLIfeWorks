package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for handling user registration data.
 * Carries the necessary information from the client (frontend)
 * to the backend for creating a new user account.
 */
@Data
public class RegistrationRequest {
	
	/**
     * Username for the new user.
     * Cannot be blank.
     */	
	@NotBlank 
	private String username;
	
	/**
     * Email address for the user.
     * Must be a valid email format and not blank.
     */
    @NotBlank 
    @Email 
    private String email;
    
    /**
     * Password for the account.
     * Must be at least 6 characters and not blank.
     */
    @NotBlank 
    @Size(min = 6) 
    private String password;
}
