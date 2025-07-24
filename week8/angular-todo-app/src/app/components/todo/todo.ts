// src/app/components/todo/todo.ts

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

// Angular Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatBadgeModule } from '@angular/material/badge';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';

import { AuthService } from '../../services/auth';
import { TodoService } from '../../services/todo';
import { Todo, TodoRequest, TodoFilter } from '../../models/todo.interface';
import { User } from '../../models/user.interface';

/**
 * Todo component handling task management
 * Features: CRUD operations, search, filter, due dates, statistics
 */
@Component({
  selector: 'app-todo',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatChipsModule,
    MatMenuModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatBadgeModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatTooltipModule
  ],
  templateUrl: './todo.html',
  styleUrls: ['./todo.scss']
})
export class TodoComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  // User and todos data
  currentUser: User | null = null;
  todos: Todo[] = [];
  filteredTodos: Todo[] = [];
  
  // Forms
  addTodoForm!: FormGroup;
  editTodoForm!: FormGroup;
  searchForm!: FormGroup;
  
  // UI state
  isLoading = false;
  isAddingTodo = false;
  editingTodoId: string | null = null;
  currentFilter: TodoFilter = TodoFilter.ALL;
  searchTerm = '';
  sidenavOpened = false;
  
  // Statistics
  stats = { total: 0, completed: 0, pending: 0 };
  
  // Filter options
  filterOptions = [
    { value: TodoFilter.ALL, label: 'All Tasks', icon: 'list' },
    { value: TodoFilter.PENDING, label: 'Pending', icon: 'schedule' },
    { value: TodoFilter.COMPLETED, label: 'Completed', icon: 'check_circle' }
  ];

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private todoService: TodoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initializeForms();
    this.loadCurrentUser();
    this.loadTodos();
    this.setupSearchSubscription();
    this.loadTodoStats();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Initialize reactive forms
   */
  private initializeForms(): void {
    // Add todo form
    this.addTodoForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(200)]],
      description: ['', [Validators.maxLength(500)]],
      dueDate: ['']
    });

    // Edit todo form
    this.editTodoForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(200)]],
      description: ['', [Validators.maxLength(500)]],
      dueDate: ['']
    });

    // Search form
    this.searchForm = this.formBuilder.group({
      searchTerm: ['']
    });
  }
  getCurrentFilterIcon(): string {
  const match = this.filterOptions.find(f => f.value === this.currentFilter);
  return match ? match.icon : 'list';
}

  /**
   * Load current user information
   */
  private loadCurrentUser(): void {
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.currentUser = user;
        if (!user) {
          this.router.navigate(['/login']);
        }
      });
  }

  /**
   * Load todos from service
   */
  private loadTodos(): void {
    this.isLoading = true;
    this.todoService.getTodos()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (todos) => {
          this.todos = todos;
          this.applyFiltersAndSearch();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading todos:', error);
          this.showError('Failed to load todos');
          this.isLoading = false;
        }
      });
  }

  /**
   * Load todo statistics
   */
  private loadTodoStats(): void {
    this.todoService.getTodosStats()
      .pipe(takeUntil(this.destroy$))
      .subscribe(stats => {
        this.stats = stats;
      });
  }

  /**
   * Setup search functionality with debouncing
   */
  private setupSearchSubscription(): void {
    this.searchForm.get('searchTerm')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(searchTerm => {
        this.searchTerm = searchTerm || '';
        this.applyFiltersAndSearch();
      });
  }

  /**
   * Apply current filter and search term to todos
   */
  private applyFiltersAndSearch(): void {
    let filtered = this.todoService.filterTodos(this.todos, this.currentFilter);
    
    if (this.searchTerm.trim()) {
      filtered = this.todoService.searchTodos(filtered, this.searchTerm);
    }
    
    this.filteredTodos = filtered;
  }

  /**
   * Add a new todo
   */
  onAddTodo(): void {
    if (this.addTodoForm.valid) {
      this.isAddingTodo = true;
      
      const todoRequest: TodoRequest = {
        title: this.addTodoForm.value.title.trim(),
        description: this.addTodoForm.value.description?.trim() || '',
        dueDate: this.addTodoForm.value.dueDate || undefined
      };

      this.todoService.addTodo(todoRequest)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (newTodo) => {
            this.addTodoForm.reset();
            this.loadTodos();
            this.loadTodoStats();
            this.showSuccess('Todo added successfully!');
            this.isAddingTodo = false;
          },
          error: (error) => {
            console.error('Error adding todo:', error);
            this.showError('Failed to add todo');
            this.isAddingTodo = false;
          }
        });
    }
  }

  /**
   * Start editing a todo
   * @param todo - Todo to edit
   */
  startEditTodo(todo: Todo): void {
    this.editingTodoId = todo.id;
    this.editTodoForm.patchValue({
      title: todo.title,
      description: todo.description || '',
      dueDate: todo.dueDate || ''
    });
  }

  /**
   * Save edited todo
   */
  saveEditTodo(): void {
    if (this.editTodoForm.valid && this.editingTodoId) {
      const updates: Partial<Todo> = {
        title: this.editTodoForm.value.title.trim(),
        description: this.editTodoForm.value.description?.trim() || '',
        dueDate: this.editTodoForm.value.dueDate || undefined
      };

      this.todoService.updateTodo(this.editingTodoId, updates)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.cancelEditTodo();
            this.loadTodos();
            this.showSuccess('Todo updated successfully!');
          },
          error: (error) => {
            console.error('Error updating todo:', error);
            this.showError('Failed to update todo');
          }
        });
    }
  }

  /**
   * Cancel editing todo
   */
  cancelEditTodo(): void {
    this.editingTodoId = null;
    this.editTodoForm.reset();
  }

  /**
   * Toggle todo completion status
   * @param todo - Todo to toggle
   */
  toggleTodoCompletion(todo: Todo): void {
    this.todoService.toggleTodoCompletion(todo.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.loadTodos();
          this.loadTodoStats();
          const message = todo.completed ? 'Todo marked as pending' : 'Todo completed!';
          this.showSuccess(message);
        },
        error: (error) => {
          console.error('Error toggling todo:', error);
          this.showError('Failed to update todo');
        }
      });
  }

  /**
   * Delete a todo
   * @param todo - Todo to delete
   */
  deleteTodo(todo: Todo): void {
    if (confirm(`Are you sure you want to delete "${todo.title}"?`)) {
      this.todoService.deleteTodo(todo.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.loadTodos();
            this.loadTodoStats();
            this.showSuccess('Todo deleted successfully!');
          },
          error: (error) => {
            console.error('Error deleting todo:', error);
            this.showError('Failed to delete todo');
          }
        });
    }
  }

  /**
   * Change current filter
   * @param filter - New filter to apply
   */
  changeFilter(filter: TodoFilter): void {
    this.currentFilter = filter;
    this.applyFiltersAndSearch();
    this.sidenavOpened = false; // Close sidenav on mobile after selection
  }

  /**
   * Clear search
   */
  clearSearch(): void {
    this.searchForm.patchValue({ searchTerm: '' });
  }

  /**
   * Clear all completed todos
   */
  clearCompletedTodos(): void {
    const completedTodos = this.todos.filter(todo => todo.completed);
    
    if (completedTodos.length === 0) {
      this.showInfo('No completed todos to clear');
      return;
    }

    if (confirm(`Are you sure you want to delete ${completedTodos.length} completed todo(s)?`)) {
      Promise.all(
        completedTodos.map(todo => 
          this.todoService.deleteTodo(todo.id).toPromise()
        )
      ).then(() => {
        this.loadTodos();
        this.loadTodoStats();
        this.showSuccess(`${completedTodos.length} completed todos cleared!`);
      }).catch((error) => {
        console.error('Error clearing completed todos:', error);
        this.showError('Failed to clear completed todos');
      });
    }
  }

  /**
   * Get priority class for todo based on due date
   * @param todo - Todo to check
   * @returns CSS class string
   */
  getTodoPriorityClass(todo: Todo): string {
    if (!todo.dueDate) return '';
    
    const now = new Date();
    const dueDate = new Date(todo.dueDate);
    const diffTime = dueDate.getTime() - now.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays < 0) return 'overdue';
    if (diffDays === 0) return 'due-today';
    if (diffDays === 1) return 'due-tomorrow';
    if (diffDays <= 3) return 'due-soon';
    
    return '';
  }

  /**
   * Format due date for display
   * @param dueDate - Date to format
   * @returns Formatted date string
   */
  formatDueDate(dueDate: Date): string {
    const now = new Date();
    const due = new Date(dueDate);
    const diffTime = due.getTime() - now.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays < 0) return `${Math.abs(diffDays)} day(s) overdue`;
    if (diffDays === 0) return 'Due today';
    if (diffDays === 1) return 'Due tomorrow';
    if (diffDays <= 7) return `Due in ${diffDays} day(s)`;
    
    return due.toLocaleDateString();
  }

  /**
   * Get current filter label
   */
  getCurrentFilterLabel(): string {
    const filter = this.filterOptions.find(f => f.value === this.currentFilter);
    return filter ? filter.label : 'All Tasks';
  }

  /**
   * Logout user
   */
  logout(): void {
    if (confirm('Are you sure you want to logout?')) {
      this.authService.logout();
      this.showSuccess('Logged out successfully');
      this.router.navigate(['/login']);
    }
  }

  /**
   * Toggle sidenav
   */
  toggleSidenav(): void {
    this.sidenavOpened = !this.sidenavOpened;
  }

  /**
   * Show success message
   */
  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  /**
   * Show error message
   */
  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  /**
   * Show info message
   */
  private showInfo(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      panelClass: ['info-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  /**
   * Check if form field has error
   */
  hasError(formName: 'add' | 'edit', fieldName: string): boolean {
    const form = formName === 'add' ? this.addTodoForm : this.editTodoForm;
    const control = form.get(fieldName);
    return !!(control && control.errors && control.touched);
  }

  /**
   * Track by function for ngFor optimization
   */
  trackByTodoId(index: number, todo: Todo): string {
    return todo.id;
  }
  /**
 * Get error message for a specific form and field
 * @param formName 'add' | 'edit'
 * @param fieldName name of the field (e.g. 'title')
 */
getErrorMessage(formName: 'add' | 'edit', fieldName: string): string {
  const form = formName === 'add' ? this.addTodoForm : this.editTodoForm;
  const control = form.get(fieldName);

  if (!control || !control.errors) return '';

  if (control.errors['required']) {
    return 'This field is required';
  }

  if (control.errors['minlength']) {
    const requiredLength = control.errors['minlength'].requiredLength;
    return `Minimum ${requiredLength} character(s) required`;
  }

  if (control.errors['maxlength']) {
    const requiredLength = control.errors['maxlength'].requiredLength;
    return `Maximum ${requiredLength} character(s) allowed`;
  }

  return 'Invalid field';
}
}