import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { TaskDto, TaskStatusUpdateDto, CreateTaskDto } from '../../shared/models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  // Base URL for all task-related API endpoints
  private apiUrl = `${environment.apiBaseUrl}/tasks`;

  constructor(private http: HttpClient) { }

  // Creates a new task by sending a POST request with task data
  createTask(task: CreateTaskDto): Observable<TaskDto> {
    return this.http.post<TaskDto>(this.apiUrl, task).pipe(
      catchError(this.handleError)
    );
  }

  // Retrieves all tasks by sending a GET request
  getTasks(): Observable<TaskDto[]> {
    return this.http.get<TaskDto[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  // Retrieves tasks assigned to the current user
  getAssignedTasks(): Observable<TaskDto[]> {
    return this.http.get<TaskDto[]>(`${this.apiUrl}/assigned`)
      .pipe(catchError(this.handleError));
  }

  // Updates the status of a task (e.g., To Do, In Progress, Completed)
  updateTaskStatus(taskId: number, status: string): Observable<TaskDto> {
    // Prepare payload with the new status, restricting it to allowed values
    const statusDto: TaskStatusUpdateDto = { status: status as 'To Do' | 'In Progress' | 'Completed' };
    // Send PUT request to update task status
    return this.http.put<TaskDto>(`${this.apiUrl}/${taskId}/status`, statusDto)
      .pipe(catchError(this.handleError));
  }

  // Handles HTTP errors and returns an Observable that emits a user-friendly error message
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
    }
    console.error(errorMessage); // Log error message in console for debugging
    return throwError(() => new Error(errorMessage));
  }
}
