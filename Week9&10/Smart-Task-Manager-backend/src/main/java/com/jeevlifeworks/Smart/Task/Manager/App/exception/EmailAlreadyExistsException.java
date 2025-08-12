package com.jeevlifeworks.Smart.Task.Manager.App.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Custom exception thrown when a user tries to register with an email
 * that already exists in the system.
 *
 * The @ResponseStatus annotation maps this exception to a 409 CONFLICT HTTP status code.
 */
@ResponseStatus(HttpStatus.CONFLICT) // Sends HTTP 409 Conflict when this exception is thrown
public class EmailAlreadyExistsException extends RuntimeException{
	
	/**
     * Constructor to create the exception with a custom error message.
     * 
     * Accepts parameter message Detailed error message for the exception.
     */
	public EmailAlreadyExistsException(String message) {
        super(message); // Pass message to RuntimeException constructor
    }
}
