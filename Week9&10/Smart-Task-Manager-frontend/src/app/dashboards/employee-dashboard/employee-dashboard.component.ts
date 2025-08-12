import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../tasks/services/task.service';
import { TaskDto, TaskStatus } from '../../shared/models/task.model';
import { ToastrService } from 'ngx-toastr';
import { ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';


@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.scss'],
  standalone: true,
  imports: [CommonModule , FormsModule]
})
export class EmployeeDashboardComponent implements OnInit {
  // Stores the list of tasks assigned to the logged-in employee
  tasks: TaskDto[] = [];
  // Possible statuses a task can have
  taskStatuses: TaskStatus[] = ['To Do', 'In Progress', 'Completed'];
  loading: boolean = true; // Used to show a loading spinner
  errorMessage: string | null = null; // Stores error messages

  constructor(
    private router: Router,
    private taskService: TaskService,
    private toastr: ToastrService
  ) {}

  /**
   * Angular lifecycle hook
   * Runs automatically when the component is initialized
   */
  ngOnInit(): void {
    this.loadAssignedTasks(); // Load all tasks assigned to the logged-in employee
    // this.loadDashboardAnalytics();
  }

  /**
   * Fetches all tasks assigned to the logged-in employee from the backend
   */
  loadAssignedTasks(): void {
    this.taskService.getAssignedTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks; // Store tasks in component variable
        this.loading = false; // Stop loading indicator
      },
      error: (err) => {
        this.errorMessage = 'Failed to load assigned tasks. Please try again later.';
        this.loading = false;
        console.error('Error fetching assigned tasks', err);
      }
    });
  }

  /**
   * Updates the status of a specific task
   * Accepts parameter taskId - The ID of the task being updated
   * Accepts parameter event - The HTML select change event containing the new status
   */
  updateTaskStatus(taskId: number, event: Event): void {
    const status = (event.target as HTMLSelectElement).value as TaskStatus;
    this.taskService.updateTaskStatus(taskId, status).subscribe({
      next: () => {
        this.toastr.success('Task status updated successfully.');
        this.loadAssignedTasks(); // Reload tasks after updating
      },
      error: (err) => this.toastr.error('Failed to update task status: ' + err.message)
    });
  }

  /**
   * Logs the user out by removing token and redirecting to login page
   */
  logout(): void {
    localStorage.removeItem('authToken'); // Remove JWT token
    this.router.navigate(['/login']); // Redirect to login page
  }
}
