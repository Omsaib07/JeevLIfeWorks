package com.jeevlifeworks.Smart.Task.Manager.App.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.PasswordResetToken;

/**
 * Repository interface for PasswordResetToken entity.
 * Provides methods to manage password reset tokens.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	/**
     * Finds a PasswordResetToken entity by its token string.
     * 
     * This is typically used when a user clicks a password reset link
     * and the application needs to verify the token.
     *
     * Accepts parameter token The token string received via email or frontend
     * return Optional containing the PasswordResetToken if found
     */
	Optional<PasswordResetToken> findByToken(String token);
}
