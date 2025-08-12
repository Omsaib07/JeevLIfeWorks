// Angular environment configuration file for the development environment.
// This file stores environment-specific variables such as API URLs and flags.
// During a production build, Angular will replace this file with `environment.prod.ts`
// using the `fileReplacements` array in `angular.json`.

// Exported constant holding environment settings
export const environment = {
  // Indicates that the app is running in development mode (not production)
  production: false,

  // Base URL for your backend API in development mode
  // This is used throughout the app to make HTTP requests to your backend
  apiBaseUrl: 'http://localhost:8080/api' 
};
