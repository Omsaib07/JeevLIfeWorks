package com.user.service;

//Importing the User model/entity
import com.user.model.User;
//Importing the UserRepository interface to perform DB operations
import com.user.repository.UserRepository;

//Lombok annotation to auto-generate constructor for final fields
import lombok.RequiredArgsConstructor;
//Spring Security's utility for encoding passwords
import org.springframework.security.crypto.password.PasswordEncoder;
//Marks this class as a Spring-managed service component
import org.springframework.stereotype.Service;

import java.util.Optional;

//Indicates that this class is a Spring service component (business logic layer)
@Service
//Lombok annotation to generate a constructor for the final fields (Dependency Injection)
@RequiredArgsConstructor
public class UserService {

 // Repository to interact with the database for User entities
 private final UserRepository userRepository;

 // Password encoder to securely hash passwords before storing them
 private final PasswordEncoder passwordEncoder;

 /**
  * Saves a new user to the database after encoding the password.
  * @param user the User entity to save
  * @return the saved User entity
  */
 public User save(User user) {
     // Encode the raw password before saving
     user.setPassword(passwordEncoder.encode(user.getPassword()));
     // Save the user in the database and return the saved entity
     return userRepository.save(user);
 }

 /**
  * Finds a user by username.
  * @param username the username to search for
  * @return an Optional containing the User if found, or empty if not
  */
 public Optional<User> findByUsername(String username) {
     return userRepository.findByUsername(username);
 }
 
 /**
  * Checks if a user with the given username already exists.
  * @param username the username to check
  * @return true if the user exists, false otherwise
  */
 public boolean existsByUsername(String username) {
     // Returns true if user with the given username is found
     return userRepository.findByUsername(username).isPresent();
 }

}

