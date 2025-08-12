package com.jeevlifeworks.Smart.Task.Manager.App.Entity;


/**
 * RoleEnum defines the available user roles in the application.
 * This enum is used to control access levels for different users.
 */
public enum RoleEnum {
	
	// ROLE_ADMIN: Has full control, including user and role management.
	ROLE_ADMIN,
	// ROLE_MANAGER: Can create and assign tasks, monitor progress.
    ROLE_MANAGER,
    // ROLE_EMPLOYEE: Can view and update their own tasks.
    ROLE_EMPLOYEE
}
