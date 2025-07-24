// src/app/services/todo.spec.ts

import { TestBed } from '@angular/core/testing';
import { TodoService } from './todo';
import { AuthService } from './auth';
import { of } from 'rxjs';

describe('TodoService', () => {
  let service: TodoService;
  let mockAuthService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    // Create a mock AuthService
    const authSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);
    authSpy.getCurrentUser.and.returnValue({ id: 'user1' });

    TestBed.configureTestingModule({
      providers: [
        TodoService,
        { provide: AuthService, useValue: authSpy }
      ]
    });

    service = TestBed.inject(TodoService);
    mockAuthService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a todo and return it', (done) => {
    const todoRequest = {
      title: 'Test Todo',
      description: 'Test Description',
      dueDate: new Date()
    };

    service.addTodo(todoRequest).subscribe(todo => {
      expect(todo.title).toBe('Test Todo');
      expect(todo.description).toBe('Test Description');
      expect(todo.completed).toBeFalse();
      done();
    });
  });

  it('should return todos for the current user', (done) => {
    service.getTodos().subscribe(todos => {
      expect(Array.isArray(todos)).toBeTrue();
      done();
    });
  });
});