// This file defines the application-wide configuration for an Angular standalone app.
// It sets up routing, animations, HTTP interceptors, and toast notifications.
// This configuration is passed to `bootstrapApplication()` in main.ts.

import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

// Correctly import the 'routes' constant from the app-routing module
import { routes } from './app-routing.module';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
// HTTP interceptor for attaching JWT tokens to requests
import { AuthInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // Enables Angular Router and passes in the route definitions
    provideRouter(routes),
    provideAnimations(),
    provideToastr({
      timeOut: 10000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
    provideHttpClient(withInterceptors([AuthInterceptor])),
  ],
};