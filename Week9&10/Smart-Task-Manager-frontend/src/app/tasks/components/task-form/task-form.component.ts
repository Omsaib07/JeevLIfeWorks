import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { TaskDto , CreateTaskDto} from '../../../shared/models/task.model';
import { User } from '../../../shared/models/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class TaskFormComponent implements OnInit {
  // Input property to receive list of employees to assign task to
  @Input() employees: User[] = [];
  // Output event emitter to notify parent component when a task is created
  @Output() taskCreated = new EventEmitter<void>();

  // Reactive form group to manage task form fields and validations
  taskForm!: FormGroup;
  // Possible task priority options
  priorities = ['Low', 'Medium', 'High'];

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private toastr: ToastrService
  ) {}  

  // Lifecycle hook - initialize the reactive form with default values and validators
  ngOnInit(): void {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['Medium', Validators.required],
      assigneeIds: [[], Validators.required],
      tags: ['']
    });
  }

  // Called when the form is submitted
  onSubmit(): void {
    // Proceed only if form is valid
    if (this.taskForm.valid) {
      // Create a new task object from form values
      const newTask: CreateTaskDto = {
        ...this.taskForm.value,
        status: 'To Do', // Default status for new tasks
        managerId: 1 // TODO: Get the actual manager ID from the authenticated user
      };

      // Call service to create the task via backend API
      this.taskService.createTask(newTask).subscribe({
        next: (response) => {
          console.log('Task created successfully', response);
          this.taskForm.reset(); // Reset the form after successful submission
          this.taskCreated.emit(); // Notify parent component that a task was created
        },
        error: (error) => {
          console.error('Error creating task', error); // Log any error
        }
      });
    }
  }
}
