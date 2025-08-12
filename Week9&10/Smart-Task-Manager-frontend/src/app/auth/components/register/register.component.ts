import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router'; // For navigation and routerLink directive
import { AuthService } from '../../services/auth.service'; // Service for authentication API calls
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true, 
  imports: [FormsModule, RouterLink] // Required modules for template features
})
export class RegisterComponent {
  // Object to hold user input from registration form, bound using ngModel
  user = {
    username: '',
    email: '',
    password: ''
  };

  // Inject AuthService for registration API, Router for navigation, ToastrService for notifications
  constructor(
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  // Called when the registration form is submitted
  onSubmit(): void {
    // Call register method of AuthService, passing the user details
    this.authService.register(this.user).subscribe({
      next: (response) => {
        // Show success toast notification after successful registration
        this.toastr.success('Registration successful! Please check your email to activate your account.');
        // Redirect user to login page
        this.router.navigate(['/login']);
      },
      error: (err) => {
        // Show error toast notification if registration fails
        this.toastr.error('Registration failed: ' + err.message);
        // Log the error to console for debugging
        console.error('Registration failed:', err);
      }
    });
  }
}