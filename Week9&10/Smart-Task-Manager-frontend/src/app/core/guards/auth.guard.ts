
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';
import { ToastrService } from 'ngx-toastr';

/**
 * Helper function to decode a JWT token.
 * Accepts parameter token The JWT token string.
 * returns An object containing the decoded payload.
 */
function decodeJwt(token: string): any {
  try {
    // JWT format: header.payload.signature
    // We want the payload (index 1)
    const payload = token.split('.')[1];
    // Decode the base64-encoded payload
    const decodedPayload = atob(payload);
    // Convert JSON string → JS object
    return JSON.parse(decodedPayload);
  } catch (e) {
    console.error('Error decoding JWT token:', e);
    return null;
  }
}

/**
 * Angular Functional Route Guard (AuthGuard)
 * 
 * Purpose:
 * - Ensures that the user is logged in (token exists)
 * - Ensures that the user has the correct role to access a route
 * 
 * Usage:
 * - Applied in `app-routing.module.ts` via `canActivate: [AuthGuard]`
 * - Can use `data: { roles: ['ROLE_ADMIN'] }` to restrict by role
 */
export const AuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); // For checking login state & retrieving token
  const router = inject(Router); // For navigation
  const toastr = inject(ToastrService); // For showing error messages

  // 1. Check if a token exists
  const token = authService.getToken();
  if (!token) {
    toastr.error('You must be logged in to view this page.');
    router.navigate(['/login']);
    return false;
  }

  // 2. Decode the token to get the user's roles
  const decodedToken = decodeJwt(token);
  if (!decodedToken || !decodedToken.roles) {
    toastr.error('Invalid user token or missing role information.');
    router.navigate(['/login']);
    return false;
  }

  // 3. Get the required roles from the route data
  const requiredRoles = route.data['roles'] as string[];
  const userRoles = decodedToken.roles as string[]; // Roles from token

  // 4. Check if the user has at least one of the required roles
  const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));

  if (hasRequiredRole) {
    // The user has the correct role, allow access
    return true;
  } else {
    // The user does not have the required role
    toastr.error('Access Denied. You do not have the required role to view this page.');
    router.navigate(['/login']);
    return false; // ❌ Access denied
  }

};