// This is the root Angular module that bootstraps the entire application.
// It imports all necessary Angular modules, third-party libraries, and core application components.

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'; // For making HTTP requests to the backend
import { FormsModule } from '@angular/forms'; // Template-driven forms support
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // Required for animations
import { ToastrModule } from 'ngx-toastr'; // For toast notifications
// Application routing
import { AppRoutingModule } from './app-routing.module';
// Root and feature components
import { AppComponent } from './app.component';
import { LoginComponent } from './auth/components/login/login.component';
import { RegisterComponent } from './auth/components/register/register.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { AdminDashboardComponent } from './dashboards/admin-dashboard/admin-dashboard.component';
import { ManagerDashboardComponent } from './dashboards/manager-dashboard/manager-dashboard.component';
import { EmployeeDashboardComponent } from './dashboards/employee-dashboard/employee-dashboard.component';

@NgModule({
  // No traditional declarations here since this app uses standalone components
  declarations: [],
  
  // Modules and standalone components to import into the application
  imports: [
    BrowserModule, // Required for running Angular in the browser
    HttpClientModule, // Enables HTTP requests
    FormsModule, // Enables template-driven forms
    BrowserAnimationsModule,

    // Standalone components used in the root module
    AppComponent,
    LoginComponent,
    RegisterComponent,
    AdminDashboardComponent,
    ManagerDashboardComponent,
    EmployeeDashboardComponent,

    // Toast notifications configuration
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
  ],

  // Services and providers (empty because standalone components handle most services)
  providers: [],
})
export class AppModule { }