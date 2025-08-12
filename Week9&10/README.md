# Smart Task Manager

## Overview

Smart Task Manager is a full-stack web application for efficient task management within organizations. It features **role-based access control** (Admin, Manager, Employee), secure authentication, task assignment, status tracking, notifications, and analytics dashboards. The system is built with **Spring Boot** (backend) and **Angular** (frontend).

---

## Features

### Authentication & Authorization
- User registration with email verification
- JWT-based login/logout and session management
- "Forgot Password" and password reset via email
- Role-based access (Admin, Manager, Employee)

### Task Management
- Managers create and assign tasks to employees
- Tasks include title, description, due date, priority, tags, and status (To Do, In Progress, Blocked, Completed)
- Employees update status of assigned tasks
- Filtering by status, due date, assignee, and priority

### Notifications
- Email/UI notifications for new tasks
- Daily reminders for upcoming/overdue tasks
- Notification settings per user

### Dashboard & Analytics
- Personalized dashboards for each role
- Task statistics, pie/bar charts, deadlines, and overdue highlights
- Downloadable reports (PDF/Excel) by status, user, or date range

### User & Role Management (Admin)
- Add, edit, delete users
- Assign/modify roles
- View user activity logs

---

## Technologies

**Backend:**  
- Spring Boot, Spring Security (JWT), Spring Data JPA, MySQL/PostgreSQL/H2  
- JavaMailSender for email  
- Swagger/OpenAPI for API docs

**Frontend:**  
- Angular 16+  
- Angular Material for UI  
- ngx-toastr for notifications  
- Chart.js/ngx-charts for analytics

---

## Getting Started

### Prerequisites

- **Backend:** Java 17+, Maven, MySQL/PostgreSQL (or H2 for dev)
- **Frontend:** Node.js 16+, npm, Angular CLI

---

### Backend Setup

1. **Configure Database:**  
   Edit `Smart-Task-Manager-backend/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
   spring.datasource.username=your_db_user
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

2. **Configure Email (Gmail SMTP):**  
   Set your Gmail and app password:
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_gmail@gmail.com
   spring.mail.password=your_app_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. **Run the Backend:**  
   ```bash
   cd Smart-Task-Manager-backend
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080/api`.

---

### Frontend Setup

1. **Install Dependencies:**  
   ```bash
   cd Smart-Task-Manager-frontend
   npm install
   ```

2. **Configure API URL:**  
   Edit `src/environments/environment.ts`:
   ```typescript
   export const environment = {
     production: false,
     apiBaseUrl: 'http://localhost:8080/api'
   };
   ```

3. **Run the Frontend:**  
   ```bash
   ng serve
   ```
   The app will be available at `http://localhost:4200`.

---

## Usage

- **Register:** Use `/api/auth/register` to create a new user.
- **Login:** Use `/api/auth/login` to obtain a JWT token.
- **Role Management:** Admins can update user roles via `/api/users/{userId}/role`.
- **Task Management:** Managers assign tasks; employees update status.
- **Notifications:** Email and UI notifications for task events.
- **Dashboard:** View analytics and export reports.

---

## Development Notes

- **Do not commit sensitive files:**  
  Add `node_modules/`, `dist/`, `.env`, `.DS_Store`, and IDE folders to `.gitignore`.
- **API Documentation:**  
  Swagger UI available at `/swagger-ui.html` when backend is running.
- **Password Security:**  
  Passwords are hashed using BCrypt.

---

