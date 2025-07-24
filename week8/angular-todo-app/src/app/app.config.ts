// src/app/app.config.ts

import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { routes } from './app.routes';

/**
 * Application configuration
 * Provides necessary services and modules for the application
 */
export const appConfig: ApplicationConfig = {
  providers: [
    // Router configuration with defined routes
    provideRouter(routes),
    
    // Angular Material animations support
    provideAnimationsAsync(),
    
    // Material date adapter for date picker components
    provideNativeDateAdapter(),
    
    // Import Material modules that need to be available globally
    importProvidersFrom(MatSnackBarModule)
  ]
};