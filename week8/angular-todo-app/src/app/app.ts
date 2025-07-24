// src/app/app.ts

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/**
 * Root component of the application
 * Serves as the main container for all other components
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent {
  /**
   * Application title
   */
  title = 'Todo App';

  constructor() {
    // Initialize any application-wide configurations here
    this.initializeApp();
  }

  /**
   * Initialize application-wide settings
   */
  private initializeApp(): void {
    // Set application theme based on user preference or system setting
    this.setTheme();
    
    // Add any other initialization logic here
    console.log('Todo App initialized successfully');
  }

  /**
   * Set application theme
   * Currently supports light theme, can be extended for dark theme
   */
  private setTheme(): void {
    // Check if user has a theme preference stored
    const savedTheme = localStorage.getItem('todo_app_theme');
    
    if (savedTheme) {
      document.body.setAttribute('data-theme', savedTheme);
    } else {
      // Default to light theme
      document.body.setAttribute('data-theme', 'light');
    }
  }
}