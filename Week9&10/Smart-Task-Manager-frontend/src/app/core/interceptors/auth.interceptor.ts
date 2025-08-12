/**
 * AuthInterceptor
 * ----------------
 * Purpose:
 * - Automatically attaches the JWT token to every outgoing HTTP request
 *   so that secured backend APIs can validate the user.
 * 
 * How it works:
 * - Runs before each HTTP request is sent.
 * - Retrieves the stored JWT from AuthService.
 * - Clones the request and adds an `Authorization` header with the token.
 * - Passes the cloned request to the next handler in the chain.
 */

import { HttpInterceptorFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService); // Get AuthService instance
  const authToken = authService.getToken(); // Retrieve token from localStorage

  // If we have a token, attach it; if not, just forward the original request
  const authReq = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${authToken}`)
  });

  // Forward the (possibly modified) request to the next handler
  return next(authReq);
};
