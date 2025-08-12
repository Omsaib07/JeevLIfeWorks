/**
 * Represents a User object as received from the backend API.
 * This structure is used throughout the frontend for user-related data.
 */
export interface User {
  id: number;
  username: string;
  email: string;
  roles: Role[];
  enabled: boolean;
}

/**
 * Represents a Role object for role-based access control.
 * Each user can have one or more roles that determine what they can do in the system.
 */
export interface Role {
  id: number;
  name: 'ROLE_ADMIN' | 'ROLE_MANAGER' | 'ROLE_EMPLOYEE';
}

/**
 * Data Transfer Object (DTO) for updating a user's role.
 * Used when the admin changes a user's access level in the system.
 */
export interface UpdateUserRoleDto {
  role: 'ROLE_ADMIN' | 'ROLE_MANAGER' | 'ROLE_EMPLOYEE';
}
