// src/app/components/login/login.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { AuthService } from '../../services/auth';
import { LoginRequest } from '../../models/user.interface';
import { FormsModule } from '@angular/forms';

/**
 * Login component handling user authentication
 * Features form validation, remember me functionality, and user feedback
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule, FormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatCheckboxModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isLoading = false;
  hidePassword = true;
  rememberMe = false;
  private readonly REMEMBER_EMAIL_KEY = 'todo_app_remember_email';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadRememberedEmail();
    this.checkIfAlreadyLoggedIn();
  }

  /**
   * Initialize the login form with validators
   */
  private initializeForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(100)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(1) // Allow any length for login, validation was done during signup
      ]]
    });
  }

  /**
   * Load remembered email from localStorage if available
   */
  private loadRememberedEmail(): void {
    const rememberedEmail = localStorage.getItem(this.REMEMBER_EMAIL_KEY);
    if (rememberedEmail) {
      this.loginForm.patchValue({ email: rememberedEmail });
      this.rememberMe = true;
    }
  }

  /**
   * Check if user is already logged in and redirect to todos
   */
  private checkIfAlreadyLoggedIn(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/todos']);
    }
  }

  /**
   * Handle form submission
   */
  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      
      const loginData: LoginRequest = {
        email: this.loginForm.value.email.trim().toLowerCase(),
        password: this.loginForm.value.password
      };

      // Handle remember me functionality
      this.handleRememberMe(loginData.email);

      // Call auth service to authenticate user
      this.authService.login(loginData).subscribe({
        next: (user) => {
          this.isLoading = false;
          this.showSuccess(`Welcome back, ${user.fullName}!`);
          
          // Redirect to todos page after successful login
          setTimeout(() => {
            this.router.navigate(['/todos']);
          }, 1000);
        },
        error: (error) => {
          this.isLoading = false;
          this.showError(error.message || 'Login failed. Please check your credentials.');
          
          // Focus on password field for retry
          const passwordField = document.querySelector('input[formControlName="password"]') as HTMLInputElement;
          if (passwordField) {
            passwordField.focus();
            passwordField.select();
          }
        }
      });
    } else {
      // Mark all fields as touched to show validation errors
      this.markFormGroupTouched();
    }
  }

  /**
   * Handle remember me functionality
   * @param email - Email to remember or forget
   */
  private handleRememberMe(email: string): void {
    if (this.rememberMe) {
      localStorage.setItem(this.REMEMBER_EMAIL_KEY, email);
    } else {
      localStorage.removeItem(this.REMEMBER_EMAIL_KEY);
    }
  }

  /**
   * Mark all form controls as touched to trigger validation display
   */
  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Toggle password visibility
   */
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  /**
   * Toggle remember me checkbox
   */
  toggleRememberMe(): void {
    this.rememberMe = !this.rememberMe;
  }

  /**
   * Get error message for a specific form field
   * @param fieldName - Name of the form field
   * @returns Error message string
   */
  getErrorMessage(fieldName: string): string {
    const control = this.loginForm.get(fieldName);
    
    if (!control || !control.errors || !control.touched) {
      return '';
    }

    const errors = control.errors;

    switch (fieldName) {
      case 'email':
        if (errors['required']) return 'Email is required';
        if (errors['email']) return 'Please enter a valid email address';
        if (errors['maxlength']) return 'Email is too long';
        break;

      case 'password':
        if (errors['required']) return 'Password is required';
        if (errors['minlength']) return 'Password is required';
        break;
    }

    return '';
  }

  /**
   * Check if a specific field has an error
   * @param fieldName - Name of the form field
   * @returns Boolean indicating if field has error
   */
  hasError(fieldName: string): boolean {
    const control = this.loginForm.get(fieldName);
    return !!(control && control.errors && control.touched);
  }

  /**
   * Fill demo credentials for testing
   */
  fillDemoCredentials(): void {
    this.loginForm.patchValue({
      email: 'demo@example.com',
      password: 'demo123'
    });
    this.showInfo('Demo credentials filled. Note: You need to signup with these credentials first.');
  }

  /**
   * Navigate to signup page
   */
  goToSignup(): void {
    this.router.navigate(['/signup']);
  }

  /**
   * Handle forgot password (placeholder functionality)
   */
  forgotPassword(): void {
    const email = this.loginForm.get('email')?.value;
    if (email) {
      this.showInfo(`Password reset instructions would be sent to ${email} (Demo feature)`);
    } else {
      this.showInfo('Please enter your email address first');
    }
  }

  /**
   * Show success message
   * @param message - Success message to display
   */
  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  /**
   * Show error message
   * @param message - Error message to display
   */
  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  /**
   * Show info message
   * @param message - Info message to display
   */
  private showInfo(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      panelClass: ['info-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  /**
   * Handle Enter key press for quick login
   * @param event - Keyboard event
   */
  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && this.loginForm.valid && !this.isLoading) {
      this.onSubmit();
    }
  }
}