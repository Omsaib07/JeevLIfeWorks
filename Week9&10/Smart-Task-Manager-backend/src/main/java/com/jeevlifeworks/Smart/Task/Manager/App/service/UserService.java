package com.jeevlifeworks.Smart.Task.Manager.App.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Role;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.RoleRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.UserRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Change the role of a specific user by user ID.
     *
     * Accepts parameter userId   the ID of the user
     * Accepts parameter roleName the name of the new role (e.g., ADMIN, MANAGER, EMPLOYEE)
     * return the updated User object
     */
    public User changeUserRole(Long userId, String roleName) {
    	log.info("Attempting to change role for user ID "+ userId +" to "+ roleName);
    	// Fetch the user by ID
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.error("User with ID "+ userId +" not found.");
                return new ResourceNotFoundException("User not found with id: " + userId);
            });

        Set<Role> newRoles = new HashSet<>();
     // Convert the role name to enum format (e.g., "ADMIN" -> "ROLE_ADMIN")
        String newRoleName = roleName.toUpperCase();
        if(!newRoleName.startsWith("ROLE_")) {
        	newRoleName = "ROLE_"+ newRoleName;
        }
        
        RoleEnum newRoleEnum = RoleEnum.valueOf(newRoleName);
     // Fetch the Role entity from the repository
        Role role = roleRepository.findByName(newRoleEnum)
            .orElseThrow(() -> {
                log.error("Role "+ newRoleEnum +" not found.");
                return new ResourceNotFoundException("Role not found: " + newRoleEnum);
            });

     // Assign the new role to the user
        newRoles.add(role);
        user.setRoles(newRoles);

     // Save and return the updated user
        User updatedUser = userRepository.save(user);
        log.info("Role for user ID "+ userId +" successfully changed to "+ roleName);
        return updatedUser;
    }

    /**
     * Retrieve all users from the system.
     *
     * return list of all users
     */
    public List<User> getAllUsers() {
        log.info("Fetching all users.");
        return userRepository.findAll();
    }

    /**
     * Retrieve only users with the EMPLOYEE role.
     *
     * return list of employees
     */
    public List<User> getEmployees() {
        log.info("Fetching all employees.");
        return userRepository.findByRoles_Name(RoleEnum.ROLE_EMPLOYEE);
    }

    /**
     * Fetch the current authenticated user using their UserDetails.
     *
     * Accepts parameter userDetails the Spring Security user details object
     * return the authenticated User
     */
    public User getCurrentUser(UserDetails userDetails) {
        log.debug("Fetching current user details for email: "+ userDetails.getUsername());
        return userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> {
                log.error("Authenticated user not found in database: "+ userDetails.getUsername());
                return new ResourceNotFoundException("Authenticated user not found.");
            });
    }

    /**
     * Delete a user by their ID.
     *
     * Accepts parameter userId the ID of the user to delete
     */
    public void deleteUser(Long userId) {
        log.info("Attempting to delete user with ID: "+ userId);
        if (!userRepository.existsById(userId)) {
            log.error("Delete failed: User with ID "+ userId +" not found.");
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("User with ID "+ userId +" successfully deleted.");
    }
}
