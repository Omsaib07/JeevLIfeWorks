package com.jeevlifeworks.Smart.Task.Manager.App.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;

/**
 * Repository interface for User entity.
 * Provides database access methods related to User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	/**
     * Finds a user by their email.
     *
     * Accepts parameter email The email address to search for.
     * return Optional containing the User if found.
     */
	Optional<User> findByEmail(String email);
	
	/**
     * Checks if a user exists with the given email.
     *
     * Accepts parameter email The email address to check.
     * return true if user exists, false otherwise.
     */
	boolean existsByEmail(String email);
	
	/**
     * Finds all users having a specified role.
     *
     * Accepts parameter roleName The RoleEnum to filter users by (e.g., ROLE_MANAGER).
     * return List of Users having the specified role.
     */
	List<User> findByRoles_Name(RoleEnum roleName);
}
