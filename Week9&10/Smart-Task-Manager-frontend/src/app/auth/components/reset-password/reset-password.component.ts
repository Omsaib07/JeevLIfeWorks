import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router'; // To access query params, navigate, and routerLink directive
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
  standalone: true,
  imports: [FormsModule, RouterLink]
})
export class ResetPasswordComponent implements OnInit {
  // Token received from query parameters in the URL used to verify the reset request
  token: string | null = null;
  // Bound to the input fields for the new password and its confirmation
  newPassword: string = '';
  confirmPassword: string = '';

  constructor(
    private route: ActivatedRoute,  // To read query parameters from URL
    private authService: AuthService,  // To call backend API for resetting password
    private router: Router,  // To navigate between routes
    private toastr: ToastrService  // To show toast notifications
  ) { }

  // Lifecycle hook - runs once the component initializes
  ngOnInit(): void {
    // Subscribe to query parameters to get the 'token' from URL
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];

      // If no token is found in URL, show error and redirect to login page
      if (!this.token) {
        this.toastr.error('Invalid or missing password reset token.');
        this.router.navigate(['/login']);
      }
    });
  }

  // Called when the reset password form is submitted
  onSubmit(): void {
    // Check if the new password and confirmation match
    if (this.newPassword !== this.confirmPassword) {
      this.toastr.error('Passwords do not match.');
      return; // Stop further execution if passwords don't match
    }

    // Proceed only if token exists and new password is not empty
    if (this.token && this.newPassword) {
      // Call the API to reset password, passing the token and new password
      this.authService.resetPassword(this.token, this.newPassword).subscribe({
        next: () => {
          // Show success notification on successful reset
          this.toastr.success('Password has been reset successfully. You can now log in with your new password.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          // Show error notification if reset fails
          this.toastr.error('Password reset failed. The link may be invalid or expired.');
          console.error('Password reset error:', err);
        }
      });
    }
  }
}