import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { User, UpdateUserRoleDto } from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // Base URL for user-related API endpoints
  private apiUrl = `${environment.apiBaseUrl}/users`;

  constructor(private http: HttpClient) { }

  // Get the list of all users
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  // Update the role of a specific user by their userId
  updateUserRole(userId: number, roleDto: UpdateUserRoleDto): Observable<User> {
    // Sending a PUT request with the new role as a query parameter
    // Body is empty ({}), because the role is passed via query params
    return this.http.put<User>(`${this.apiUrl}/${userId}/role?role=${roleDto.role}`,{})
      .pipe(catchError(this.handleError));
  }

  // Delete a user by their userId
  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`)
      .pipe(catchError(this.handleError));
  }

  // Common method to handle HTTP errors for all API calls
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
