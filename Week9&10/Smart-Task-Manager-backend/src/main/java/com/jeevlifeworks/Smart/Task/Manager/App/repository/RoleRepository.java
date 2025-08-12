package com.jeevlifeworks.Smart.Task.Manager.App.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Role;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum;

/**
 * Repository interface for Role entity.
 * Provides database operations related to Role.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	/**
     * Finds a Role entity by its enum name.
     * 
     * Accepts name, The RoleEnum to search for (e.g., ROLE_ADMIN)
     * returns an Optional containing the Role if found, else empty.
     */
	Optional<Role> findByName(RoleEnum name);
}
