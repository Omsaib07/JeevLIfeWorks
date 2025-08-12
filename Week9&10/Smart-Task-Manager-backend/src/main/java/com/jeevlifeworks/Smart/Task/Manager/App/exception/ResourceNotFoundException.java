package com.jeevlifeworks.Smart.Task.Manager.App.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a requested resource is not found.
 *
 * The @ResponseStatus annotation maps this exception to a 404 NOT FOUND HTTP status code.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Returns HTTP 404 when this exception is thrown
public class ResourceNotFoundException extends RuntimeException{
	
	/**
     * Constructor to create the exception with a custom error message.
     * 
     * Accepts parameter message Detailed message indicating which resource was not found.
     */
	public ResourceNotFoundException(String message) {
        super(message); // Pass the custom message to the base RuntimeException
    }
}
