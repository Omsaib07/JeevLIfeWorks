package com.jeevlifeworks.Smart.Task.Manager.App.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * Entity to handle email verification tokens.
 * Used during registration to verify user's email address.
 */
@Entity
@Data
public class VerificationToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    private String token; // Unique token sent via email

    /**
     * One-to-one association with the User entity.
     * Each verification token is linked to a single user.
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate; // Expiration date for token
}
