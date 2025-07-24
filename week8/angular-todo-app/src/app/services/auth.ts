import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { User, LoginRequest, SignupRequest } from '../models/user.interface';

/**
 * Authentication service handling user signup, login, logout operations
 * Simulates backend authentication using localStorage
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USERS_KEY = 'todo_app_users';
  private readonly CURRENT_USER_KEY = 'todo_app_current_user';
  
  // BehaviorSubject to track current user state
  private currentUserSubject = new BehaviorSubject<User | null>(this.getCurrentUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    // Initialize users array in localStorage if it doesn't exist
    if (!localStorage.getItem(this.USERS_KEY)) {
      localStorage.setItem(this.USERS_KEY, JSON.stringify([]));
    }
  }

  /**
   * Sign up a new user
   * @param signupData - User signup information
   * @returns Observable with success message or error
   */
  signup(signupData: SignupRequest): Observable<{ message: string }> {
    try {
      const users = this.getUsersFromStorage();
      
      // Check if user with this email already exists
      const existingUser = users.find(user => user.email === signupData.email);
      if (existingUser) {
        return throwError(() => new Error('User with this email already exists'));
      }

      // Create new user object
      const newUser: User = {
        id: this.generateUserId(),
        fullName: signupData.fullName,
        email: signupData.email,
        password: signupData.password, // In real app, this should be hashed
        createdAt: new Date()
      };

      // Add user to storage
      users.push(newUser);
      localStorage.setItem(this.USERS_KEY, JSON.stringify(users));

      return of({ message: 'User registered successfully' });
    } catch (error) {
      return throwError(() => new Error('Signup failed. Please try again.'));
    }
  }

  /**
   * Authenticate user login
   * @param loginData - User login credentials
   * @returns Observable with user data or error
   */
  login(loginData: LoginRequest): Observable<User> {
    try {
      const users = this.getUsersFromStorage();
      
      // Find user with matching email and password
      const user = users.find(u => 
        u.email === loginData.email && u.password === loginData.password
      );

      if (!user) {
        return throwError(() => new Error('Invalid email or password'));
      }

      // Store current user in localStorage and update subject
      localStorage.setItem(this.CURRENT_USER_KEY, JSON.stringify(user));
      this.currentUserSubject.next(user);

      return of(user);
    } catch (error) {
      return throwError(() => new Error('Login failed. Please try again.'));
    }
  }

  /**
   * Log out current user
   */
  logout(): void {
    localStorage.removeItem(this.CURRENT_USER_KEY);
    this.currentUserSubject.next(null);
  }

  /**
   * Check if user is currently authenticated
   * @returns boolean indicating authentication status
   */
  isAuthenticated(): boolean {
    return this.getCurrentUserFromStorage() !== null;
  }

  /**
   * Get current authenticated user
   * @returns Current user or null
   */
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Retrieve users from localStorage
   * @returns Array of users
   */
  private getUsersFromStorage(): User[] {
    try {
      const usersJson = localStorage.getItem(this.USERS_KEY);
      return usersJson ? JSON.parse(usersJson) : [];
    } catch (error) {
      console.error('Error reading users from storage:', error);
      return [];
    }
  }

  /**
   * Get current user from localStorage
   * @returns Current user or null
   */
  private getCurrentUserFromStorage(): User | null {
    try {
      const userJson = localStorage.getItem(this.CURRENT_USER_KEY);
      return userJson ? JSON.parse(userJson) : null;
    } catch (error) {
      console.error('Error reading current user from storage:', error);
      return null;
    }
  }

  /**
   * Generate unique user ID
   * @returns Unique string ID
   */
  private generateUserId(): string {
    return 'user_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
  }
}