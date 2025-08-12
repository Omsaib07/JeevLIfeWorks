package com.jeevlifeworks.Smart.Task.Manager.App.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User entity maps to the 'users' table and represents
 * an application user. Users can be assigned roles and tasks.
 * A user can also act as a manager assigning tasks to others.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString(exclude = {"tasksAssigned", "roles"}) // Prevents recursive calls
@EqualsAndHashCode(exclude = {"tasksAssigned", "roles"}) // Avoid circular reference in hash
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary Key
	
	@NotBlank
    private String username; // Unique username
	
	@Email
	@NotBlank
	private String email; // Must be a valid email
	
	@NotBlank
	private String password; // Encrypted password
	
	private boolean isEnabled = false; // Account activation flag
	
	/**
     * Many-to-Many relation with Role.
     * A user can have multiple roles (Admin, Manager, Employee).
     */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", // Join table
	      joinColumns = @JoinColumn(name = "user_id"), // Foreign key from User
	      inverseJoinColumns = @JoinColumn(name = "role_id")) // Foreign key from Role
	private Set<Role> roles = new HashSet<>();
	
	/**
     * One-to-Many relationship with Task entity.
     * Represents tasks this user has created/assigned (as Manager).
     */	
	@OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Task> tasksAssigned;
	
}
