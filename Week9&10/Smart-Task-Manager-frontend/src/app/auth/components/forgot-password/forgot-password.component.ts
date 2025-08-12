import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
  standalone: true,
  imports: [FormsModule, RouterLink]
})
export class ForgotPasswordComponent {
  // Property bound to email input field
  email: string = '';

  // Inject AuthService for API calls and ToastrService for notifications
  constructor(
    private authService: AuthService,
    private toastr: ToastrService
  ) { }

  // Called when user submits the forgot password form
  onSubmit(): void {
    // Only proceed if email is not empty
    if (this.email) {
      // Call backend API to request password reset link
      this.authService.requestPasswordReset(this.email).subscribe({
        next: () => {
          // Show success message regardless of email existence for security reasons
          this.toastr.success('If an account with that email exists, a password reset link has been sent.');
          this.email = ''; // Clear email input after submission
        },
        error: (err) => {
          // Show error message if request fails
          this.toastr.error('Error requesting password reset. Please try again.');
          console.error('Forgot password error:', err);
        }
      });
    }
  }
}