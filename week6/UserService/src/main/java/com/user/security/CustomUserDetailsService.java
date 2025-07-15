package com.user.security;

//Importing the UserRepository to fetch user details from the database
import com.user.repository.UserRepository;

//Lombok annotation to auto-generate a constructor for all final fields (Dependency Injection)
import lombok.RequiredArgsConstructor;

//Importing Spring Security's User class (implements UserDetails)
import org.springframework.security.core.userdetails.User;
//Interface required by Spring Security to load user-specific data during authentication
import org.springframework.security.core.userdetails.UserDetails;
//Interface method return type representing authenticated user details
import org.springframework.security.core.userdetails.UserDetailsService;
//Exception thrown when a user is not found during authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//Marks this class as a Spring-managed service component
import org.springframework.stereotype.Service;

import java.util.Collections;

//Marks this as a service bean and enables Spring to discover it via component scanning
@Service
//Lombok annotation to auto-generate constructor for the final userRepository field
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

 // Injected UserRepository to interact with the database
 private final UserRepository userRepository;

 // This method is called by Spring Security during authentication to load user details by username
 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     // Attempt to find the user in the database using the repository
     // If not found, throw a UsernameNotFoundException
     com.user.model.User user = userRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("User not found"));

     // Return a Spring Security User object with username, password, and empty authorities list
     return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
 }
}
