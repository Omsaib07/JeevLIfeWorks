import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth';

/**
 * Authentication guard to protect routes that require user authentication
 * Redirects unauthenticated users to the login page
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  /**
   * Determines if a route can be activated based on authentication status
   * @returns boolean or UrlTree for redirection
   */
  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // Check if user is authenticated
    if (this.authService.isAuthenticated()) {
      return true; // Allow access to the route
    }

    // User is not authenticated, redirect to login page
    console.log('User not authenticated, redirecting to login');
    return this.router.createUrlTree(['/login']);
  }
}