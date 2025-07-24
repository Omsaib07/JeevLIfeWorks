// src/app/app.routes.ts

import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth-guard';

/**
 * Application routes configuration
 * Defines navigation paths and their associated components
 */
export const routes: Routes = [
  // Default route - redirect to login
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },

  // Login route - accessible to all users
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then(m => m.LoginComponent),
    title: 'Login - Todo App'
  },

  // Signup route - accessible to all users
  {
    path: 'signup',
    loadComponent: () => import('./components/signup/signup').then(m => m.SignupComponent),
    title: 'Sign Up - Todo App'
  },

  // Todos route - protected by AuthGuard
  {
    path: 'todos',
    loadComponent: () => import('./components/todo/todo').then(m => m.TodoComponent),
    canActivate: [AuthGuard],
    title: 'My Todos - Todo App'
  },

  // Wildcard route - redirect to login for any unmatched routes
  {
    path: '**',
    redirectTo: '/login'
  }
];