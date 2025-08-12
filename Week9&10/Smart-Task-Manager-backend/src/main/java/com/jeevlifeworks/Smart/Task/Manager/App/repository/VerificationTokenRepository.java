package com.jeevlifeworks.Smart.Task.Manager.App.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.VerificationToken;

/**
 * Repository interface for managing VerificationToken entities.
 * Used for email verification during user registration.
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	
	/**
     * Finds a verification token by its token string.
     *
     * Accepts parameter token The verification token string (usually sent via email)
     * return Optional containing the VerificationToken if found
     */
	Optional<VerificationToken> findByToken(String token);
}
