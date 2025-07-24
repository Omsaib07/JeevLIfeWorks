// src/app/services/todo.ts

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Todo, TodoRequest, TodoFilter } from '../models/todo.interface';
import { AuthService } from './auth';

/**
 * Todo service handling CRUD operations for todo items
 * Stores data in localStorage and provides reactive updates
 */
@Injectable({
  providedIn: 'root'
})
export class TodoService {
  private readonly TODOS_KEY = 'todo_app_todos';
  
  // BehaviorSubject to provide reactive todo list updates
  private todosSubject = new BehaviorSubject<Todo[]>(this.getTodosFromStorage());
  public todos$ = this.todosSubject.asObservable();

  constructor(private authService: AuthService) {}

  /**
   * Get all todos for the current user
   * @returns Observable array of todos
   */
  getTodos(): Observable<Todo[]> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      return of([]);
    }

    const allTodos = this.getTodosFromStorage();
    const userTodos = allTodos.filter(todo => todo.userId === currentUser.id);
    
    // Sort todos by due date (if exists) and then by creation date
    const sortedTodos = userTodos.sort((a, b) => {
      // First, sort by due date (tasks with due dates come first)
      if (a.dueDate && !b.dueDate) return -1;
      if (!a.dueDate && b.dueDate) return 1;
      if (a.dueDate && b.dueDate) {
        return new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime();
      }
      // If both have no due date, sort by creation date (newest first)
      return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
    });

    this.todosSubject.next(sortedTodos);
    return of(sortedTodos);
  }

  /**
   * Add a new todo item
   * @param todoRequest - Todo data to create
   * @returns Observable with created todo
   */
  addTodo(todoRequest: TodoRequest): Observable<Todo> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      throw new Error('User not authenticated');
    }

    const newTodo: Todo = {
      id: this.generateTodoId(),
      title: todoRequest.title.trim(),
      description: todoRequest.description?.trim() || '',
      completed: false,
      createdAt: new Date(),
      dueDate: todoRequest.dueDate,
      userId: currentUser.id!
    };

    const todos = this.getTodosFromStorage();
    todos.push(newTodo);
    localStorage.setItem(this.TODOS_KEY, JSON.stringify(todos));

    // Refresh the todos list
    this.getTodos().subscribe();
    
    return of(newTodo);
  }

  /**
   * Update an existing todo item
   * @param todoId - ID of todo to update
   * @param updates - Partial todo data to update
   * @returns Observable with updated todo
   */
  updateTodo(todoId: string, updates: Partial<Todo>): Observable<Todo> {
    const todos = this.getTodosFromStorage();
    const todoIndex = todos.findIndex(todo => todo.id === todoId);

    if (todoIndex === -1) {
      throw new Error('Todo not found');
    }

    // Update the todo with new data
    todos[todoIndex] = { ...todos[todoIndex], ...updates };
    localStorage.setItem(this.TODOS_KEY, JSON.stringify(todos));

    // Refresh the todos list
    this.getTodos().subscribe();

    return of(todos[todoIndex]);
  }

  /**
   * Toggle completion status of a todo
   * @param todoId - ID of todo to toggle
   * @returns Observable with updated todo
   */
  toggleTodoCompletion(todoId: string): Observable<Todo> {
    const todos = this.getTodosFromStorage();
    const todo = todos.find(t => t.id === todoId);

    if (!todo) {
      throw new Error('Todo not found');
    }

    return this.updateTodo(todoId, { completed: !todo.completed });
  }

  /**
   * Delete a todo item
   * @param todoId - ID of todo to delete
   * @returns Observable with success status
   */
  deleteTodo(todoId: string): Observable<boolean> {
    const todos = this.getTodosFromStorage();
    const filteredTodos = todos.filter(todo => todo.id !== todoId);
    
    if (todos.length === filteredTodos.length) {
      throw new Error('Todo not found');
    }

    localStorage.setItem(this.TODOS_KEY, JSON.stringify(filteredTodos));

    // Refresh the todos list
    this.getTodos().subscribe();

    return of(true);
  }

  /**
   * Filter todos based on completion status
   * @param todos - Array of todos to filter
   * @param filter - Filter type (all, pending, completed)
   * @returns Filtered array of todos
   */
  filterTodos(todos: Todo[], filter: TodoFilter): Todo[] {
    switch (filter) {
      case TodoFilter.COMPLETED:
        return todos.filter(todo => todo.completed);
      case TodoFilter.PENDING:
        return todos.filter(todo => !todo.completed);
      case TodoFilter.ALL:
      default:
        return todos;
    }
  }

  /**
   * Search todos by title or description
   * @param todos - Array of todos to search
   * @param searchTerm - Search term
   * @returns Filtered array of todos matching search term
   */
  searchTodos(todos: Todo[], searchTerm: string): Todo[] {
    if (!searchTerm.trim()) {
      return todos;
    }

    const term = searchTerm.toLowerCase().trim();
    return todos.filter(todo => 
      todo.title.toLowerCase().includes(term) ||
      (todo.description && todo.description.toLowerCase().includes(term))
    );
  }

  /**
   * Get todos statistics for current user
   * @returns Object with todo statistics
   */
  getTodosStats(): Observable<{total: number, completed: number, pending: number}> {
    return new Observable(observer => {
      this.getTodos().subscribe(todos => {
        const stats = {
          total: todos.length,
          completed: todos.filter(t => t.completed).length,
          pending: todos.filter(t => !t.completed).length
        };
        observer.next(stats);
        observer.complete();
      });
    });
  }

  /**
   * Clear all todos for current user (useful for testing)
   */
  clearAllTodos(): Observable<boolean> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      throw new Error('User not authenticated');
    }

    const allTodos = this.getTodosFromStorage();
    const otherUsersTodos = allTodos.filter(todo => todo.userId !== currentUser.id);
    
    localStorage.setItem(this.TODOS_KEY, JSON.stringify(otherUsersTodos));
    this.todosSubject.next([]);

    return of(true);
  }

  /**
   * Retrieve todos from localStorage
   * @returns Array of todos
   */
  private getTodosFromStorage(): Todo[] {
    try {
      const todosJson = localStorage.getItem(this.TODOS_KEY);
      if (!todosJson) {
        return [];
      }
      
      const todos = JSON.parse(todosJson);
      // Convert date strings back to Date objects
      return todos.map((todo: any) => ({
        ...todo,
        createdAt: new Date(todo.createdAt),
        dueDate: todo.dueDate ? new Date(todo.dueDate) : undefined
      }));
    } catch (error) {
      console.error('Error reading todos from storage:', error);
      return [];
    }
  }

  /**
   * Generate unique todo ID
   * @returns Unique string ID
   */
  private generateTodoId(): string {
    return 'todo_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
  }
}