package com.jeevlifeworks.Smart.Task.Manager.App.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Task;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.TaskDto;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.TaskFilterDto;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.TaskStatusUpdateDto;
import com.jeevlifeworks.Smart.Task.Manager.App.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing task-related operations such as creating tasks,
 * updating task status, and filtering tasks based on criteria.
 */
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
	
	@Autowired 
	private TaskService taskService;

	/**
     * Endpoint to create a new task.
     * Accessible only by MANAGER or ADMIN roles.
     *
     * Accepts parameter taskDto the task details received from client
     * return the created Task entity
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
    	log.info("Received request to create a new task.");
        Task createdTask = taskService.createTask(taskDto);
        return ResponseEntity.ok(createdTask);
    }

    /**
     * Endpoint for an employee to update the status of a task assigned to them.
     * Accessible only by EMPLOYEE role.
     *
     * Accepts parameter taskId the ID of the task to update
     * Accepts parameter statusUpdate contains the new status
     * Accepts parameter userDetails the authenticated user's details
     * return the updated Task entity
     */
    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatusUpdateDto statusUpdate, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received request to update status for task ID "+ taskId +" to " + statusUpdate.getStatus());
        Task updatedTask = taskService.updateTaskStatus(taskId, statusUpdate.getStatus(), userDetails);
        return ResponseEntity.ok(updatedTask);
    }
    
    /**
     * Endpoint to filter tasks based on various criteria like status, priority, date, etc.
     * Accessible by ADMIN, MANAGER, or EMPLOYEE roles.
     *
     * Accepts parameter filterDto DTO containing filter criteria
     * Accepts parameter userDetails the authenticated user's details
     * return list of tasks matching the filter criteria
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<Task>> getTasksFiltered(@ModelAttribute TaskFilterDto filterDto, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received request to get tasks with filters.");
        List<Task> tasks = taskService.getTasksFiltered(filterDto, userDetails);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Endpoint to get a list of all tasks.
     * Accessible by ADMIN and MANAGER roles.
     *
     * @return a list of all Task entities.
     */
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<List<Task>> getAllTasks() {
		log.info("Received request to get all tasks.");
		List<Task> tasks = taskService.getAllTasks();
		return ResponseEntity.ok(tasks);
	}
	
	/**
     * Endpoint to get all tasks assigned to the currently authenticated user.
     * Accessible by MANAGER or EMPLOYEE roles.
     *
     * @return a list of TaskDto objects assigned to the current user.
     */
    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<TaskDto>> getAssignedTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // This will be the user's email
        log.info("Received request for tasks assigned to user: {}", currentUsername);

        List<TaskDto> assignedTasks = taskService.getAssignedTasksForUser(currentUsername);
        return ResponseEntity.ok(assignedTasks);
    }
}
