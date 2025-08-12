package com.jeevlifeworks.Smart.Task.Manager.App.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Task;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.TaskDto;
import com.jeevlifeworks.Smart.Task.Manager.App.dto.TaskFilterDto;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.TaskRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.repository.UserRepository;
import com.jeevlifeworks.Smart.Task.Manager.App.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {
	
	@Autowired 
	private TaskRepository taskRepository;
	
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired 
    private EmailService emailService;

    /**
     * Creates a new task and assigns it to selected users.
     * Sends notification emails to the assignees upon successful creation.
     */
    @Transactional
    public Task createTask(TaskDto taskDto) {
    	log.info("Attempting to create a new task with title: "+ taskDto.getTitle());
    	// Get the currently authenticated user (assumed to be a manager)
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User manager = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> {
                log.error("Authenticated user (manager) not found.");
                return new ResourceNotFoundException("Authenticated user not found.");
            });

     // Create and populate the Task entity
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        task.setStatus("To Do"); // Default status for new tasks
        task.setManager(manager);

     // Map assignee IDs to User entities
        List<User> assignees = taskDto.getAssigneeIds().stream()
            .map(id -> userRepository.findById(id).orElseThrow(() -> {
                log.error("Assignee with ID "+ id +" not found.");
                return new ResourceNotFoundException("Assignee not found with id: " + id);
            }))
            .collect(Collectors.toList());
        task.setAssignees(assignees);

     // Save task to database
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: "+ savedTask.getId());

        // Send task assignment email to all assignees
        assignees.forEach(assignee -> {
            log.debug("Sending task assignment notification to "+ assignee.getEmail());
            emailService.sendTaskAssignmentNotification(assignee.getEmail(), savedTask.getTitle(), manager.getUsername());
        });

        return savedTask;
    }

    /**
     * Updates the status of a task by an employee who is assigned to it.
     * Ensures only assignees can update task statuses.
     */
    @Transactional
    public Task updateTaskStatus(Long taskId, String newStatus, UserDetails userDetails) {
        log.info("Attempting to update status for task ID "+ taskId +" to "+ newStatus);
     // Fetch the task
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> {
                log.error("Task with ID "+ taskId +" not found.");
                return new ResourceNotFoundException("Task not found with id: " + taskId);
            });

     // Get current user (assumed to be an employee)
        User employee = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> {
                log.error("Authenticated user (employee) not found.");
                return new ResourceNotFoundException("Authenticated user not found.");
            });

     // Check if the user is assigned to the task
        boolean isAssignee = task.getAssignees().stream().anyMatch(a -> a.getId().equals(employee.getId()));
        if (!isAssignee) {
            log.warn("User "+ employee.getEmail() +" attempted to update task "+ taskId +" but is not an assignee.");
            throw new SecurityException("You are not authorized to update this task.");
        }

     // Update and save the new task status
        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        log.info("Task ID "+ taskId +" status successfully updated to " + newStatus);
        return updatedTask;
    }

    /**
     * Retrieves a list of tasks filtered by status, priority, assignee, and due date.
     * Employees only get their own tasks; managers/admins get tasks based on filters.
     */
    public List<Task> getTasksFiltered(TaskFilterDto filterDto, UserDetails userDetails) {
        log.info("Fetching tasks with filter: status="+ filterDto.getStatus() +", priority="+ filterDto.getPriority() +", assigneeId="+ filterDto.getAssigneeId() +", dueDate="+filterDto.getDueDate());
     // Identify current user
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> {
                log.error("User with email "+ userDetails.getUsername() +" not found.");
                return new ResourceNotFoundException("User not found");
            });

     // If the user is an employee, return only their assigned tasks
        if (currentUser.getRoles().stream().anyMatch(r -> r.getName().equals(com.jeevlifeworks.Smart.Task.Manager.App.Entity.RoleEnum.ROLE_EMPLOYEE))) {
            log.debug("User is an employee. Retrieving only their assigned tasks.");
            return taskRepository.findByAssigneeId(currentUser.getId());
        }

     // Otherwise, apply filter criteria for admin/manager
        log.debug("User is a manager or admin. Retrieving tasks based on filter criteria.");
        return taskRepository.findByFilterCriteria(
            filterDto.getStatus(),
            filterDto.getPriority(),
            filterDto.getAssigneeId(),
            filterDto.getDueDate()
        );
    }

    /**
     * Scheduled job that runs daily to send email reminders for overdue tasks.
     * Triggered based on cron expression defined in application properties.
     */
    @Scheduled(cron = "${task.reminders.schedule}")
    public void sendDailyOverdueReminders() {
        log.info("Scheduled task: Running daily overdue task reminder job.");
     // Get today's date
        LocalDate today = LocalDate.now();
     // Find tasks that are past due and not yet completed
        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusIsNot(today, "Completed");
        if (overdueTasks.isEmpty()) {
            log.info("No overdue tasks found today.");
            return;
        }

        log.info("Found "+ overdueTasks.size() +" overdue tasks.");
     // Send reminder to each assignee of overdue tasks
        for (Task task : overdueTasks) {
            task.getAssignees().forEach(assignee -> {
                log.debug("Sending overdue reminder to "+ assignee.getEmail() +" for task: "+task.getTitle());
                emailService.sendOverdueTaskReminder(assignee.getEmail(), task.getTitle(), task.getDueDate());
            });
        }
    }
    
    public List<Task> getAllTasks() {
    	log.info("Attempting to retrieve all the tasks.");
        return taskRepository.findAll();
    }
    
    /**
     * Finds and returns a list of tasks assigned to a specific user.
     *
     * Accepts parameter username The email of the user whose tasks are to be fetched.
     * return a list of TaskDto objects assigned to the user.
     */
    public List<TaskDto> getAssignedTasksForUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        List<Task> assignedTasks = taskRepository.findByAssigneeId(user.getId());

        // Convert the entity list to a DTO list
        return assignedTasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a Task entity to a TaskDto.
     */
    private TaskDto convertToDto(Task task) {
    	TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setPriority(task.getPriority());
        taskDto.setStatus(task.getStatus());
        
        // Map the manager's ID from the Task entity, with a null check
        if (task.getManager() != null) {
            taskDto.setManagerId(task.getManager().getId());
        }
        
        // Map the assignee IDs from the Task entity
        taskDto.setAssigneeIds(task.getAssignees().stream()
                                  .map(User::getId)
                                  .collect(Collectors.toList()));
                                  
        // Map the tags from the Task entity
        taskDto.setTags(task.getTags());
        return taskDto;
    }
}
