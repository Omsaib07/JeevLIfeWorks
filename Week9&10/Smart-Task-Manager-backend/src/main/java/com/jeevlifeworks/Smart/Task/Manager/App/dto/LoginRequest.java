package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for handling user login requests.
 * Carries login credentials (email and password)
 * from the client to the backend authentication endpoint.
 */
@Data
public class LoginRequest {
	
	/**
     * Email address of the user trying to log in.
     * Must be a valid format and cannot be blank.
     */
	@NotBlank 
	@Email 
	private String email;
	
	/**
     * Password for the user login.
     * Cannot be blank.
     */
    @NotBlank 
    private String password;
}
