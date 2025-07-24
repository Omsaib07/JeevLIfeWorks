
/**
 * Todo item interface defining the structure of a task
 */
export interface Todo {
  id: string;            // Unique identifier for the todo item
  title: string;         // Task title (mandatory)
  description?: string;  // Optional task description
  completed: boolean;    // Task completion status
  createdAt: Date;       // Timestamp when task was created
  dueDate?: Date;        // Optional due date for the task (bonus feature)
  userId: string;        // ID of the user who created this task
}

/**
 * Filter options for todo list display
 */
export enum TodoFilter {
  ALL = 'all',
  PENDING = 'pending',
  COMPLETED = 'completed'
}

/**
 * Request interface for creating/updating todos
 */
export interface TodoRequest {
  title: string;         // Task title
  description?: string;  // Optional task description
  dueDate?: Date;        // Optional due date
}