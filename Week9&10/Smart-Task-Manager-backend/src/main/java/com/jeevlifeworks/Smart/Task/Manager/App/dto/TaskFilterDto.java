package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * DTO to capture filtering criteria for querying tasks.
 * Used by frontend to specify filters when requesting filtered task lists.
 */
@Data
public class TaskFilterDto {

	// Filter tasks by status (e.g., To Do, Completed)
	private String status;
	// Filter tasks by priority (e.g., High, Low)
	private String priority;
	// Filter tasks assigned to a specific user by their ID
	private Long assigneeId;
	// Filter tasks with a specific due date
	private LocalDate dueDate;
}
