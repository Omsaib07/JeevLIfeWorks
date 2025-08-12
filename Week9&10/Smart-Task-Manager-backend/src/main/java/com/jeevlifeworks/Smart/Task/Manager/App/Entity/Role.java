package com.jeevlifeworks.Smart.Task.Manager.App.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role entity maps to the 'roles' table in the database.
 * It represents the role assigned to users for authorization.
 * Each role is defined by a unique ID and a name (from RoleEnum).
 */	

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key for the role entity	
	
	@Enumerated(EnumType.STRING)
	private RoleEnum name; // Stores role name as a string (e.g., "ROLE_ADMIN")
}
