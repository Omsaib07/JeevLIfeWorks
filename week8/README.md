# Todo App - Angular Task Management Application

A modern, responsive todo application built with Angular 17+, featuring user authentication, task management, and advanced filtering capabilities.

## 🚀 Features

### Core Features
- **User Authentication**: Secure signup and login system
- **Task Management**: Create, read, update, and delete todos
- **Real-time Validation**: Frontend form validation with detailed error messages
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices

### Advanced Features (Bonus)
- **Search Functionality**: Search todos by title or description
- **Smart Filters**: Filter by All, Pending, or Completed tasks
- **Due Date Management**: Set and track due dates for tasks
- **Priority Sorting**: Tasks sorted by due date and creation time
- **Task Statistics**: View completion statistics
- **Data Persistence**: All data stored in localStorage

### UI/UX Features
- **Material Design**: Beautiful, modern interface using Angular Material
- **Dark/Light Theme Support**: Automatic theme detection
- **Accessibility**: WCAG compliant with keyboard navigation
- **Loading States**: Smooth loading indicators
- **Error Handling**: User-friendly error messages
- **Responsive Layout**: Adaptive design for all screen sizes

## 🛠 Technologies Used

- **Frontend Framework**: Angular 17+ (Standalone Components)
- **UI Library**: Angular Material 17+
- **Styling**: SCSS with custom design system
- **Form Handling**: Reactive Forms with custom validators
- **Routing**: Angular Router with guards
- **State Management**: RxJS with BehaviorSubjects
- **Build Tool**: Angular CLI
- **TypeScript**: Strict mode enabled

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Node.js**: Version 18.0 or higher
- **npm**: Version 9.0 or higher (comes with Node.js)
- **Angular CLI**: Version 17.0 or higher

## 🚀 Quick Start

### 1. Install Node.js and Angular CLI

```bash
# Install Node.js from https://nodejs.org/ (LTS version recommended)

# Install Angular CLI globally
npm install -g @angular/cli
```

### 2. Create and Setup Project

```bash
# Create new Angular project
ng new todo-app --routing --style=scss --skip-git

# Navigate to project directory
cd todo-app

# Install Angular Material
ng add @angular/material

# Install additional dependencies
npm install date-fns
```

### 3. Create Directory Structure

```bash
# Create component directories
mkdir -p src/app/components/login
mkdir -p src/app/components/signup  
mkdir -p src/app/components/todo
mkdir -p src/app/guards
mkdir -p src/app/models
mkdir -p src/app/services
```

### 4. Copy Files

Copy all the provided files into their respective directories according to the tree structure:

```
src/
├── app/
│   ├── components/
│   │   ├── login/
│   │   │   ├── login.html
│   │   │   ├── login.scss
│   │   │   └── login.ts
│   │   ├── signup/
│   │   │   ├── signup.html
│   │   │   ├── signup.scss
│   │   │   └── signup.ts
│   │   └── todo/
│   │       ├── todo.html
│   │       ├── todo.scss
│   │       └── todo.ts
│   ├── guards/
│   │   └── auth-guard.ts
│   ├── models/
│   │   ├── todo.interface.ts
│   │   └── user.interface.ts
│   ├── services/
│   │   ├── auth.ts
│   │   └── todo.ts
│   ├── app.config.ts
│   ├── app.html
│   ├── app.routes.ts
│   ├── app.scss
│   └── app.ts
├── index.html
├── main.ts
└── styles.scss
```

### 5. Run the Application

```bash
# Start development server
ng serve

# Or with specific port
ng serve --port 4200

# Open browser and navigate to http://localhost:4200
```

## 📱 Usage Guide

### Getting Started

1. **First Time Setup**:
   - Navigate to `http://localhost:4200`
   - Click "Create Account" to sign up
   - Fill in your details and create an account
   - You'll be redirected to the login page

2. **Login**:
   - Enter your email and password
   - Optionally check "Remember me" to save your email
   - Click "Sign In" to access your dashboard

3. **Managing Todos**:
   - **Add Todo**: Fill in the title, optional description, and due date
   - **Complete Todo**: Click the checkbox next to any task
   - **Edit Todo**: Click the edit icon to modify a task
   - **Delete Todo**: Click the delete icon to remove a task

### Advanced Features

- **Search**: Use the search bar to find specific todos
- **Filter**: Use the sidebar to filter by All, Pending, or Completed
- **Due Dates**: Tasks are color-coded based on due date proximity
- **Statistics**: View your progress in the sidebar

## 🏗 Project Structure

```
todo-app/
├── src/
│   ├── app/
│   │   ├── components/          # UI Components
│   │   │   ├── login/          # Login component
│   │   │   ├── signup/         # Signup component
│   │   │   └── todo/           # Todo management component
│   │   ├── guards/             # Route guards
│   │   │   └── auth-guard.ts   # Authentication guard
│   │   ├── models/             # TypeScript interfaces
│   │   │   ├── user.interface.ts
│   │   │   └── todo.interface.ts
│   │   ├── services/           # Business logic services
│   │   │   ├── auth.ts         # Authentication service
│   │   │   └── todo.ts         # Todo service
│   │   ├── app.config.ts       # App configuration
│   │   ├── app.routes.ts       # Routing configuration
│   │   └── app.ts              # Root component
│   ├── index.html              # Main HTML file
│   ├── main.ts                 # Application bootstrap
│   └── styles.scss             # Global styles
├── tsconfig.json               # TypeScript configuration
├── tsconfig.app.json           # App-specific TypeScript config
├── tsconfig.spec.json          # Test TypeScript config
└── README.md                   # This file
```

## 🔧 Build and Deployment

### Development Build
```bash
ng build
```

### Production Build
```bash
ng build --configuration production
```

### Running Tests
```bash
# Unit tests
ng test

# End-to-end tests
ng e2e
```

## 🎨 Customization

### Theming
The app uses Angular Material with a custom theme. You can modify colors in:
- `src/styles.scss` - Global theme configuration
- Component-specific SCSS files for custom styling

### Adding Features
1. Create new components: `ng generate component components/feature-name`
2. Create new services: `ng generate service services/service-name`
3. Add routes in `src/app/app.routes.ts`

## 🐛 Troubleshooting

### Common Issues

1. **Angular CLI not found**:
   ```bash
   npm install -g @angular/cli
   ```

2. **Material Design icons not loading**:
   - Ensure internet connection for Google Fonts
   - Check `src/index.html` for Material Icons link

3. **Build errors**:
   ```bash
   npm install
   ng build --configuration production
   ```

4. **Port already in use**:
   ```bash
   ng serve --port 4201
   ```

### Performance Tips

- Use `ng build --prod` for production builds
- Enable service worker for caching (future enhancement)
- Optimize images and assets
- Use OnPush change detection strategy for better performance

## 🌟 Features Demonstration

### Authentication Flow
1. User registration with validation
2. Secure login with remember me functionality
3. Protected routes with authentication guards
4. Automatic logout handling

### Todo Management
1. CRUD operations with real-time updates
2. Form validation and error handling
3. Search and filter capabilities
4. Due date management and visual indicators

### UI/UX Excellence
1. Material Design principles
2. Responsive layout for all devices
3. Loading states and error handling
4. Accessibility features



## Screenshots

### Login Page
![LOGIN](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week8/Screenshots/Login.png?raw=true)

### Signup Page
![SIGNUP](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week8/Screenshots/Signup.png?raw=true)

### Todo Home Page
![HOME](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week8/Screenshots/Todo(1).png?raw=true)

### Statistics Page (Pending,etc.)
![STAT](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week8/Screenshots/Todo(2).png?raw=true)

### Add a Todo 
![ADD](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week8/Screenshots/Todo(3).png?raw=true)


