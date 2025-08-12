import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // This import is not strictly needed for @if but is good to have for other directives.
import { AuthService } from '../services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-verification',
  standalone: true,
  imports: [CommonModule], // Adding CommonModule here resolves the original error with *ngIf
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss']
})
export class VerificationComponent implements OnInit {
  // Message to display on successful verification
  message: string | null = null;
  // Error message to display if verification fails
  error: string | null = null;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute // Inject ActivatedRoute to read query parameters
  ) {}

  // Lifecycle hook - runs when the component initializes
  ngOnInit(): void {
    // Get the 'token' query parameter from the URL
    const token = this.route.snapshot.queryParams['token'];
    if (token) {
      // Call verifyAccount() method of AuthService passing the token
      this.authService.verifyAccount(token).subscribe({
        next: (response) => {
          // On success, show success message and clear error
          this.message = 'Account verified successfully! You can now log in.';
          this.error = null;
        },
        error: (err) => {
          // On failure, show error message and clear success message
          this.error = 'Verification failed. The token may be invalid or expired.';
          this.message = null;
          console.error(err);
        }
      });
    } else {
      // If no token is found in URL, show an error
      this.error = 'No verification token found.';
    }
  }
}
