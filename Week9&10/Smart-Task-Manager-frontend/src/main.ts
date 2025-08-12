/**
 * main.ts
 * ----------
 * The main entry point for the Angular application.
 * - Configures environment-specific settings (e.g., production mode).
 * - Bootstraps the root AppComponent using Angular’s standalone APIs.
 * - Sets up global providers for routing, HTTP requests, animations, and notifications.
 */

import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';

import { AppComponent } from './app/app.component';
import { AuthInterceptor } from './app/core/interceptors/auth.interceptor';
import { routes } from './app/app-routing.module';
import { environment } from './environments/environment';

// Enable production optimizations if running in production mode
if (environment.production) {
  enableProdMode();
}

// Bootstraps the root Angular application with necessary global providers
bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes), // Configures Angular Router with the application's route definitions
    provideAnimations(), // Enables animations
    provideToastr({
      positionClass: 'toast-bottom-right', // Toast position on the screen
      preventDuplicates: true, // Prevent duplicate notifications
    }),
    // Provides HTTP client with global interceptors
    provideHttpClient(withInterceptors([AuthInterceptor])), // AuthInterceptor adds JWT tokens to requests
  ]
  // Log any bootstrapping errors to the console
}).catch(err => console.error(err));