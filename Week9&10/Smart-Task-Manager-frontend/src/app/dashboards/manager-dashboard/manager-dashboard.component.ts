import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../tasks/services/task.service';
import { UserService } from '../../users/services/user.service';
import { TaskDto } from '../../shared/models/task.model';
import { User } from '../../shared/models/user.model';
import { TaskFormComponent } from '../../tasks/components/task-form/task-form.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.scss'],
  standalone: true,
  imports: [CommonModule, TaskFormComponent]
})
export class ManagerDashboardComponent implements OnInit {
  // Array to store all tasks created by the manager
  tasks: TaskDto[] = [];
  // Array to store employees
  employees: User[] = [];

  constructor(
    private router: Router,
    private taskService: TaskService,
    private userService: UserService,
    private toastr: ToastrService
  ) {}

  /**
   * Lifecycle hook that runs once when the component is initialized
   * Here we load both tasks and employees for display
   */
  ngOnInit(): void {
    this.loadTasks();
    this.loadEmployees();
  }

  /**
   * Fetches all tasks from the backend using TaskService
   * Assigns the fetched data to the 'tasks' array
   */
  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (data) => this.tasks = data, // On success, store the tasks
      error: (err) => this.toastr.error('Failed to load tasks: ' + err.message) // On error, show a toast
    });
  }

  /**
   * Fetches all users from the backend using UserService
   * Filters the list to only include users with the role 'ROLE_EMPLOYEE'
   */
  loadEmployees(): void {
    this.userService.getUsers().subscribe({
      next: (data) => {
        // Keep only users who have ROLE_EMPLOYEE in their roles
        this.employees = data.filter(user => user.roles.some(role => role.name === 'ROLE_EMPLOYEE'));
      },
      error: (err) => this.toastr.error('Failed to load employees: ' + err.message)
    });
  }

  /**
   * Called when a new task is successfully created from the TaskFormComponent
   * Shows a success message and reloads the task list
   */
  onTaskCreated(): void {
    this.toastr.success('Task created successfully!');
    this.loadTasks();
  }

  /**
   * Logs out the manager by removing the JWT token from localStorage
   * Then navigates to the login page
   */
  logout(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }
}
