/**
 * Represents the status of a task.
 * This is a "string literal type" which allows only these specific strings as valid values.
 */
export type TaskStatus = 'To Do' | 'In Progress' | 'Completed';

/**
 * Represents the priority level of a task.
 * Similar to TaskStatus, it restricts the values to only 'Low', 'Medium', or 'High'.
 */
export type TaskPriority = 'Low' | 'Medium' | 'High';

/**
 * Data Transfer Object (DTO) for a Task.
 * This interface defines the structure of task data we receive from or send to the backend API.
 */
export interface TaskDto {
  id?: number; // Optional because it's not present when creating a new task
  title: string;
  description: string;
  dueDate: string; // Due date for the task (string format for HTML date input compatibility)
  priority: TaskPriority;
  status: TaskStatus;
  managerId: number;
  assignees: User[];
  tags: string;
}

/**
 * DTO for updating only the status of a task.
 * Used when changing task progress without modifying other details.
 */
export interface TaskStatusUpdateDto {
  status: TaskStatus;
}

/**
 * Represents a User in the system.
 * This structure is used for both task assignment and user management.
 */
export interface User {
  id: number;
  username: string;
  email: string;
  roles: Role[];
  enabled: boolean;
}

/**
 * Represents a Role object.
 * Used in the User interface for role-based access control.
 */
export interface Role {
  id: number;
  name: 'ROLE_ADMIN' | 'ROLE_MANAGER' | 'ROLE_EMPLOYEE';
}

/**
 * DTO for creating a new task.
 * Similar to TaskDto but uses `assigneeIds` instead of full User objects
 * because the backend only needs user IDs when assigning.
 */
export interface CreateTaskDto {
  title: string;
  description: string;
  dueDate: string;
  priority: TaskPriority;
  status: TaskStatus;
  managerId: number;
  assigneeIds: number[]; // Array of user IDs for task assignment
  tags: string;
}