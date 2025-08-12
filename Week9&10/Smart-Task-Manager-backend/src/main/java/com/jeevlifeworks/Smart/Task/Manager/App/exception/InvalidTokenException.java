package com.jeevlifeworks.Smart.Task.Manager.App.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Custom exception thrown when a provided token is invalid or expired.
 *
 * The @ResponseStatus annotation ensures that a 400 BAD REQUEST response is returned
 * whenever this exception is thrown from a controller.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends RuntimeException{
	
	/**
     * Constructor to create the exception with a custom error message.
     *
     * Accepts parameter message Detailed message describing the reason for token invalidity.
     */
	 public InvalidTokenException(String message) {
	        super(message); // Pass the custom message to the base RuntimeException
	    }
}
