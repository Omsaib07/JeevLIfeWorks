import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/components/login/login.component';
import { RegisterComponent } from './auth/components/register/register.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminDashboardComponent } from './dashboards/admin-dashboard/admin-dashboard.component';
import { ManagerDashboardComponent } from './dashboards/manager-dashboard/manager-dashboard.component';
import { EmployeeDashboardComponent } from './dashboards/employee-dashboard/employee-dashboard.component';
import { ForgotPasswordComponent } from './auth/components/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './auth/components/reset-password/reset-password.component';
import { VerificationComponent } from './auth/verification/verification.component';

/**
 * Application Routes Configuration
 * 
 * Each route defines a path and the component that should
 * be displayed when that path is accessed. 
 * 
 * Some routes use `canActivate` with `AuthGuard` to restrict access 
 * based on authentication and role.
 */

export const routes: Routes = [
  // Public routes (no authentication required)
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'register/confirm', component: VerificationComponent }, // Email verification page

  // Protected routes (require authentication + role check)
  {
    path: 'admin/dashboard',
    component: AdminDashboardComponent,
    canActivate: [AuthGuard], // Only logged-in users can access
    data: { roles: ['ROLE_ADMIN'] } // Only users with 'ROLE_ADMIN'
  },
  {
    path: 'manager/dashboard',
    component: ManagerDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_MANAGER'] }
  },
  {
    path: 'employee/dashboard',
    component: EmployeeDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_EMPLOYEE'] }
  },
  // Redirect empty path to login page
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  // Wildcard route: any undefined URL will redirect to login
  { path: '**', redirectTo: 'login' }
];
// Export the routing module class
export class AppRoutingModule { }
