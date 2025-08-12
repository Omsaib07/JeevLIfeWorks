package com.jeevlifeworks.Smart.Task.Manager.App.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Task entity maps to the 'tasks' table and represents
 * a task assigned within the system. Tasks are created by
 * managers and can be assigned to multiple employees.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@ToString(exclude = "assignees") // Prevents circular reference in toString()
@EqualsAndHashCode(exclude = "assignees") // Avoids stack overflow in hash-based collections	
public class Task {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Unique identifier for each task
	
	private String title; // Title or short name of the task	
	
	@Column(columnDefinition = "TEXT")
	private String description;	// Detailed description	
	
	private LocalDate dueDate; // Due date for task completion
	
	// e.g., High, Medium, Low
	private String priority;
	
	// e.g., ToDo, In Progress, Completed
	private String status;
	
	
	/**
     * Many-to-Many relationship with User.
     * A task can have multiple assignees (employees), and a user can have multiple tasks.
     */	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "task_assignees",
	      joinColumns = @JoinColumn(name = "task_id"),
	      inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> assignees = new ArrayList<User>();
	
	/**
     * Many-to-One relationship to manager (User).
     * Each task is created by a manager.
     */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
	@JsonIgnore
    private User manager;
	
	// Optional comma-separated tags for filtering/searching
	private String tags;
	
}
