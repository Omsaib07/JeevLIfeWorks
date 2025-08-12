package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating the status of a task.
 * Used when an employee or manager updates task progress.
 */
@Data
public class TaskStatusUpdateDto {
	
	/**
     * New status of the task.
     * Must not be blank.
     * Examples: "To Do", "In Progress", "Blocked", "Completed".
     */
	@NotBlank 
	private String status;
}
