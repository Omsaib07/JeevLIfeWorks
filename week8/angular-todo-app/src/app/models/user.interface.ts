/**
 * User interface defining the structure of user data
 * Used for both signup and login operations
 */
export interface User {
  id?: string;           // Optional unique identifier for the user
  fullName: string;      // User's full name (required for signup)
  email: string;         // User's email address (used for login)
  password: string;      // User's password
  createdAt?: Date;      // Optional timestamp when user was created
}

/**
 * Login request interface for authentication
 */
export interface LoginRequest {
  email: string;         // User's email for authentication
  password: string;      // User's password for authentication
}

/**
 * Signup request interface for user registration
 */
export interface SignupRequest {
  fullName: string;      // User's full name
  email: string;         // User's email address
  password: string;      // User's password
  confirmPassword: string; // Password confirmation field
}