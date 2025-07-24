import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthService } from '../../services/auth';
import { SignupRequest } from '../../models/user.interface';

/**
 * Signup component handling user registration
 * Features form validation, password confirmation, and user feedback
 */
@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './signup.html',
  styleUrls: ['./signup.scss']
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;
  isLoading = false;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  /**
   * Initialize the signup form with validators
   */
  private initializeForm(): void {
    this.signupForm = this.formBuilder.group({
      fullName: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(50),
        this.noWhitespaceValidator
      ]],
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(100)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(50),
        this.passwordStrengthValidator
      ]],
      confirmPassword: ['', [
        Validators.required
      ]]
    }, {
      // Custom validator to check if passwords match
      validators: this.passwordMatchValidator
    });
  }

  /**
   * Custom validator to ensure passwords match
   * @param control - The form group control
   * @returns Validation error object or null
   */
  private passwordMatchValidator(control: AbstractControl): {[key: string]: any} | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    if (!password || !confirmPassword) {
      return null;
    }

    if (password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }

    // Clear the error if passwords match
    if (confirmPassword.hasError('passwordMismatch')) {
      delete confirmPassword.errors?.['passwordMismatch'];
      if (Object.keys(confirmPassword.errors || {}).length === 0) {
        confirmPassword.setErrors(null);
      }
    }

    return null;
  }

  /**
   * Custom validator to ensure no whitespace-only input
   * @param control - The form control
   * @returns Validation error object or null
   */
  private noWhitespaceValidator(control: AbstractControl): {[key: string]: any} | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { whitespace: true };
  }

  /**
   * Custom validator for password strength
   * @param control - The form control
   * @returns Validation error object or null
   */
  private passwordStrengthValidator(control: AbstractControl): {[key: string]: any} | null {
    const value = control.value || '';
    
    if (value.length < 6) {
      return null; // Let minLength validator handle this
    }

    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    
    // Password is considered weak if it doesn't have at least 2 of these criteria
    const criteriaCount = Number(hasUpperCase) + Number(hasLowerCase) + Number(hasNumeric);
    
    if (criteriaCount < 2) {
      return { weakPassword: true };
    }

    return null;
  }

  /**
   * Handle form submission
   */
  onSubmit(): void {
    if (this.signupForm.valid) {
      this.isLoading = true;
      
      const signupData: SignupRequest = {
        fullName: this.signupForm.value.fullName.trim(),
        email: this.signupForm.value.email.trim().toLowerCase(),
        password: this.signupForm.value.password,
        confirmPassword: this.signupForm.value.confirmPassword
      };

      // Call auth service to register user
      this.authService.signup(signupData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.showSuccess('Account created successfully! Please login to continue.');
          
          // Redirect to login page after successful signup
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 1500);
        },
        error: (error) => {
          this.isLoading = false;
          this.showError(error.message || 'Signup failed. Please try again.');
        }
      });
    } else {
      // Mark all fields as touched to show validation errors
      this.markFormGroupTouched();
    }
  }

  /**
   * Mark all form controls as touched to trigger validation display
   */
  private markFormGroupTouched(): void {
    Object.keys(this.signupForm.controls).forEach(key => {
      const control = this.signupForm.get(key);
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
   * Toggle confirm password visibility
   */
  toggleConfirmPasswordVisibility(): void {
    this.hideConfirmPassword = !this.hideConfirmPassword;
  }

  /**
   * Get error message for a specific form field
   * @param fieldName - Name of the form field
   * @returns Error message string
   */
  getErrorMessage(fieldName: string): string {
    const control = this.signupForm.get(fieldName);
    
    if (!control || !control.errors || !control.touched) {
      return '';
    }

    const errors = control.errors;

    switch (fieldName) {
      case 'fullName':
        if (errors['required']) return 'Full name is required';
        if (errors['minlength']) return 'Full name must be at least 2 characters';
        if (errors['maxlength']) return 'Full name cannot exceed 50 characters';
        if (errors['whitespace']) return 'Full name cannot be empty or contain only spaces';
        break;

      case 'email':
        if (errors['required']) return 'Email is required';
        if (errors['email']) return 'Please enter a valid email address';
        if (errors['maxlength']) return 'Email cannot exceed 100 characters';
        break;

      case 'password':
        if (errors['required']) return 'Password is required';
        if (errors['minlength']) return 'Password must be at least 6 characters';
        if (errors['maxlength']) return 'Password cannot exceed 50 characters';
        if (errors['weakPassword']) return 'Password should contain at least 2 of: uppercase, lowercase, numbers';
        break;

      case 'confirmPassword':
        if (errors['required']) return 'Please confirm your password';
        if (errors['passwordMismatch']) return 'Passwords do not match';
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
    const control = this.signupForm.get(fieldName);
    return !!(control && control.errors && control.touched);
  }

  /**
   * Show success message
   * @param message - Success message to display
   */
  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
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
   * Navigate to login page
   */
  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}