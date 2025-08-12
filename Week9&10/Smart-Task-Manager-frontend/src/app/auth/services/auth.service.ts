// import { Injectable } from '@angular/core';
// import { HttpClient, HttpErrorResponse } from '@angular/common/http';
// import { Observable, BehaviorSubject, throwError } from 'rxjs';
// import { catchError, map, tap } from 'rxjs/operators';
// import { jwtDecode } from 'jwt-decode';
// import { environment } from '../../../environments/environment';
// import { Router } from '@angular/router';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   private apiUrl = `${environment.apiBaseUrl}/auth`;
//   private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
//   private tokenKey = 'auth_token';

//   constructor(private http: HttpClient, private router: Router) { }

//   login(credentials: any): Observable<void> {
//     return this.http.post(`${this.apiUrl}/login`, credentials, {
//       responseType: 'text' // <-- Tell HttpClient to expect a plain text response
//     }).pipe(
//       map((token: string) => {
//         const userRoles = this.getRoles();
//         if (userRoles.includes('ROLE_ADMIN')) {
//           this.router.navigate(['/admin/dashboard']);
//         } else if (userRoles.includes('ROLE_MANAGER')) {
//           this.router.navigate(['/manager/dashboard']);
//         } else {
//           this.router.navigate(['/employee/dashboard']);
//         }
//       }),
//       catchError(this.handleError)
//     );
//   }

//   // A method to save the token to local storage
//   private saveToken(token: string): void {
//     if (typeof localStorage !== 'undefined') {
//       localStorage.setItem(this.tokenKey, token);
//     }
//   }

//   // A method to get the token from local storage
//   getToken(): string | null {
//     if (typeof localStorage !== 'undefined') {
//       return localStorage.getItem(this.tokenKey);
//     }
//     return null;
//   }

//   register(user: any): Observable<any> {
//     return this.http.post(`${this.apiUrl}/register`, user, {
//       responseType: 'text' // This is the crucial line
//     }).pipe(
//       catchError(this.handleError)
//     );
//   }

//   logout(): void {
//     localStorage.removeItem(this.tokenKey);
//     this.loggedIn.next(false);
//   }

//   isLoggedIn(): boolean {
//     return this.hasToken();
//   }

//   // getToken(): string | null {
//   //   return localStorage.getItem(this.tokenKey);
//   // }

//   getRoles(): string[] {
//     const token = this.getToken();
//     if (token) {
//       try {
//         const decodedToken: any = jwtDecode(token);
//         return decodedToken.roles || [];
//       } catch (error) {
//         console.error('Error decoding JWT token:', error);
//         return [];
//       }
//     }
//     return [];
//   }

//   requestPasswordReset(email: string): Observable<any> {
//     const payload = { email };
//     return this.http.post(`${this.apiUrl}/forgot-password`, payload, {
//       responseType: 'text'
//     }).pipe(
//       catchError(this.handleError)
//     );
//   }

//   resetPassword(token: string, newPassword: string): Observable<any> {
//     const payload = { token, newPassword };
//     return this.http.post(`${this.apiUrl}/reset-password`, payload, {
//       responseType: 'text'
//     }).pipe(
//       catchError(this.handleError)
//     );
//   }

//   private hasToken(): boolean {
//     return !!localStorage.getItem(this.tokenKey);
//   }

//   /**
//    * Private method to handle HTTP errors from API calls.
//    * This is a standard error-handling pattern in Angular services.
//    * @param error The HttpErrorResponse object.
//    * @returns An Observable that throws an error with a user-friendly message.
//    */
//   private handleError(error: HttpErrorResponse): Observable<never> {
//     let errorMessage = 'An unknown error occurred!';
//     if (error.error instanceof ErrorEvent) {
//       // A client-side or network error occurred.
//       errorMessage = `Error: ${error.error.message}`;
//     } else {
//       // The backend returned an unsuccessful response code.
//       // The response body may contain clues as to what went wrong.
//       errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
//     }
//     console.error(errorMessage);
//     return throwError(() => new Error(errorMessage));
//   }
// }

import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http'; // For HTTP requests and error handling
import { Observable, throwError } from 'rxjs'; // For reactive programming
import { catchError, map, tap } from 'rxjs/operators'; // Operators to transform and handle Observables
import { jwtDecode } from 'jwt-decode'; // To decode JWT tokens
import { environment } from '../../../environments/environment'; // To access environment variables

// Interface representing the structure of the decoded JWT token payload
interface JwtPayload {
  sub: string; // Usually the subject, e.g. username or email
  roles: string[]; // Array of user roles like ['ROLE_ADMIN', 'ROLE_USER']
}

@Injectable({
  providedIn: 'root' // This service is provided application-wide
})
export class AuthService {
  private apiUrl = `${environment.apiBaseUrl}/auth`; // Base URL for auth-related APIs
  private tokenKey = 'auth_token'; // Key used to store JWT token in localStorage

  constructor(private http: HttpClient) { }

  // Login method sends credentials and returns Observable of the user's primary role as a string
  login(credentials: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { responseType: 'text' }).pipe(
      // Save the received JWT token into localStorage
      tap((token: string) => this.saveToken(token)),

      // Decode the token to extract roles and return the first role as the user's primary role
      map((token: string) => {
        const decodedToken = this.decodeToken<JwtPayload>(token);
        if (decodedToken && decodedToken.roles && decodedToken.roles.length > 0) {
          // Assuming a user has a single primary role, return the first one.
          return decodedToken.roles[0];
        }
        return 'ROLE_EMPLOYEE'; // Default role if not found
      }),
      catchError(this.handleError)
    );
  }

  // Register method to create a new user account
  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' }).pipe(
      catchError(this.handleError)
    );
  }

  // Logs out user by removing JWT token from localStorage
  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(this.tokenKey);
    }
  }

  // Checks if user is logged in by checking existence of token
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    
    return true;
  }

  // Retrieves the JWT token from localStorage
  getToken(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem(this.tokenKey);
    }
    return null;
  }

  // Returns the roles of the logged-in user by decoding the JWT token
  getRoles(): string[] {
    const token = this.getToken();
    if (token) {
      try {
        const decodedToken: any = this.decodeToken(token);
        return decodedToken.roles || [];
      } catch (error) {
        console.error('Error decoding JWT token:', error);
        return [];
      }
    }
    return [];
  }

  // Sends a password reset request for a given email
  requestPasswordReset(email: string): Observable<any> {
    const payload = { email };
    return this.http.post(`${this.apiUrl}/forgot-password`, payload, {
      responseType: 'text'
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Sends new password and token to reset the user's password
  resetPassword(token: string, newPassword: string): Observable<any> {
    const payload = { token, newPassword };
    return this.http.post(`${this.apiUrl}/reset-password`, payload, {
      responseType: 'text'
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Verifies user account by token after registration (email verification)
  verifyAccount(token: string): Observable<string> {
    const params = new HttpParams().set('token', token);
    return this.http.get<string>(`${this.apiUrl}/register/confirm`, { params, responseType: 'text' as 'json' });
  }

  // Saves JWT token to localStorage
  private saveToken(token: string): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.tokenKey, token);
    }
  }

  // Decodes JWT token payload into specified generic type
  private decodeToken<T>(token: string): T {
    return jwtDecode(token) as T;
  }

  // Handles HTTP errors and returns a user-friendly error Observable
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Backend returned an unsuccessful response code
      errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
