package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a Task.
 * Used to transfer task details between backend and frontend.
 */
@Data
public class TaskDto {

	// Unique identifier of the task
	private Long id;
	// Title of the task
	private String title;
	// Detailed description of the task
	private String description;
	// Due date by which task should be completed
	private LocalDate dueDate;
	// Task priority (e.g., Low, Medium, High)
	private String priority;
	// Current status of the task (e.g., To Do, In Progress, Completed)
	private String status;
	// ID of the manager who created/owns the task
	private Long managerId;
	// List of user IDs assigned to this task
	private List<Long> assigneeIds;
	// Optional tags associated with the task for filtering.
	private String tags;
}
