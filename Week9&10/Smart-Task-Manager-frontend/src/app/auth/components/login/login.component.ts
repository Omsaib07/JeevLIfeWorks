import { Component } from '@angular/core';
import { Router , RouterLink} from '@angular/router'; // For navigation and routerLink directive
import { AuthService } from '../../services/auth.service'; // Service to handle authentication API calls
import { ToastrService } from 'ngx-toastr'; // Service for showing toast notifications
import { FormsModule } from '@angular/forms'; // Needed for ngModel two-way binding in template
import { CommonModule } from '@angular/common'; // Common Angular directives like ngIf, ngFor

@Component({
  selector: 'app-login',
  standalone: true,           // This makes it a standalone component
  imports: [CommonModule, FormsModule, RouterLink],  // Required modules for template features
  templateUrl: './login.component.html', //HTML template file
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  // Object to hold email and password entered by user; bound to form inputs via ngModel
  credentials = { email: '', password: '' };

  // Inject AuthService for login API, Router for navigation, ToastrService for notifications
  constructor(
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  // Called when the login form is submitted
  onLogin(): void {
    // Call login method of AuthService with entered credentials
    this.authService.login(this.credentials).subscribe({
      next: (role) => {
        // On successful login, the API returns user role
        this.toastr.success('Login successful!'); // Show success toast notification

        // Navigate to dashboard based on user role
        if (role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else if (role === 'ROLE_MANAGER') {
          this.router.navigate(['/manager/dashboard']);
        } else {
          this.router.navigate(['/employee/dashboard']);
        }
      },
      error: (error) => {
        // Show error toast notification if login fails
        this.toastr.error('Invalid email or password.', 'Login Failed');
        console.error('Login error:', error); // Log error details in browser console
      }
    });
  }
}