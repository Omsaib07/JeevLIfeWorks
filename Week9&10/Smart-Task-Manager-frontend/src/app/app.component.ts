// This is the root component of the Angular application.
// It acts as the main shell for all other components and contains the <router-outlet>
// where Angular will render the components for different routes.

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root', // The HTML tag to use this component in index.html
  standalone: true, // Marks this as a standalone component (no need to declare in NgModule)
  templateUrl: './app.component.html', // The HTML template file for the root component
  styleUrls: ['./app.component.scss'], // Styles specific to this component
  imports: [RouterOutlet] // Imports RouterOutlet so routing works in the template
})
export class AppComponent {
  // Application title
  title = 'smart-task-manager-frontend';
}