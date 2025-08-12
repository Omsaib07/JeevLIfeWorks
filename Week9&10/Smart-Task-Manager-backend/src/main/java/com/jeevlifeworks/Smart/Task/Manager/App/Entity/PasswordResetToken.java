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
 * Entity to handle password reset functionality.
 * Each token is associated with a single user and has an expiry time.
 */
@Entity
@Data
public class PasswordResetToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    private String token; // Unique token string used for reset verification

    /**
     * One-to-One relationship with User.
     * Each token is assigned to one specific user.
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate; // Token expiration time
}
