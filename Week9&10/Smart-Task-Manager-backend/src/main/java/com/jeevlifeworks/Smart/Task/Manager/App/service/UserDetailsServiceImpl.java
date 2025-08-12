package com.jeevlifeworks.Smart.Task.Manager.App.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * Responsible for loading user-specific data during the authentication process.
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
    UserRepository userRepository;

	/**
     * Loads a user by their email address.
     * This method is automatically called by Spring Security during login.
     *
     * Accepts parameter email the email (used as username) of the user to be loaded
     * return UserDetails object with user information and roles
     * throws UsernameNotFoundException if the user is not found or not verified
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	log.info("Attempting to load user with email: "+ email);
    	// Fetch the user from the database using email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("User not found with email: "+ email);
                return new UsernameNotFoundException("User Not Found with email: " + email);
            });
        
        // Check if the user's account is enabled (email verified)
        if (!user.isEnabled()) {
            log.warn("User account is not enabled for email: "+ email);
            throw new UsernameNotFoundException("User account is not enabled. Please verify your email.");
        }

        // Create a UserDetails object with the user's information and roles
        log.info("User loaded successfully: "+ email);
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList()));
    }
}
