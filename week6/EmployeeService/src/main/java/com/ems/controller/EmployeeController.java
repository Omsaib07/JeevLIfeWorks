package com.ems.controller;

// Importing the ApiResponse wrapper for consistent API response structure
import com.ems.dto.ApiResponse;
// Custom exception thrown when an employee is not found
import com.ems.exception.EmployeeNotFoundException;
// Importing the Employee entity/model
import com.ems.model.Employee;
// Importing the service layer to delegate business logic
import com.ems.service.EmployeeService;

// Lombok annotation to auto-generate constructor for final fields
import lombok.RequiredArgsConstructor;

// Spring's class for building HTTP responses
import org.springframework.http.ResponseEntity;
// Spring annotation to mark this class as a REST controller
import org.springframework.web.bind.annotation.*;
// Annotation to enable bean validation on input data
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

// Marks this class as a REST controller, automatically serializing return values to JSON
@RestController
// Base URI for all request mappings in this controller (i.e., /employees)
@RequestMapping("/employees")
// Lombok annotation that generates a constructor with required (final) fields
@RequiredArgsConstructor
public class EmployeeController {

    // Injecting the EmployeeService to handle business logic
    private final EmployeeService service;

    // Handles HTTP GET requests to "/employees"
    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getAll() {
        // Fetch all employees from the service layer
        List<Employee> employees = service.getAllEmployees();
        // Return a successful API response with the list of employees
        return ResponseEntity.ok(new ApiResponse<>("success", "Fetched all employees", employees));
    }

    // Handles HTTP GET requests to "/employees/{id}"
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getById(@PathVariable int id) {
        // Attempt to find the employee by ID; throw exception if not found
        Employee emp = service.getEmployeeById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found."));
        // Return the found employee wrapped in a success response
        return ResponseEntity.ok(new ApiResponse<>("success", "Employee found", emp));
    }

    // Handles HTTP POST requests to "/employees" to create a new employee
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody Employee employee) {
        // Validate and add the employee using service layer
        service.addEmployee(employee);
        // Return a success response with no data (null)
        return ResponseEntity.ok(new ApiResponse<>("success", "Employee created successfully", null));
    }

    // Handles HTTP PUT requests to "/employees/{id}" to update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable int id, @Valid @RequestBody Employee employee) {
        // Validate and update the employee details using service layer
        service.updateEmployee(id, employee);
        // Return a success response indicating update status
        return ResponseEntity.ok(new ApiResponse<>("success", "Employee updated successfully", null));
    }

    // Handles HTTP DELETE requests to "/employees/{id}" to delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        // Call service method to delete employee by ID
        service.deleteEmployee(id);
        // Return a success response indicating deletion
        return ResponseEntity.ok(new ApiResponse<>("success", "Employee deleted successfully", null));
    }

    // Handles GET request to "/employees/high-salary" with a query param to filter employees by salary
    @GetMapping("/high-salary")
    public ResponseEntity<ApiResponse<List<Employee>>> getHighSalary(@RequestParam double threshold) {
        // Get list of employees whose salary is above the given threshold
        List<Employee> filtered = service.filterHighSalary(threshold);
        // Return the filtered list in a success response
        return ResponseEntity.ok(new ApiResponse<>("success", "Employees with salary above " + threshold, filtered));
    }

    // Handles GET request to "/employees/group-by-department" to return employees grouped by department
    @GetMapping("/group-by-department")
    public ResponseEntity<ApiResponse<Map<String, List<Employee>>>> groupByDept() {
        // Get the map of department names to list of employees
        Map<String, List<Employee>> grouped = service.groupByDepartment();
        // Return the grouped data in a success response
        return ResponseEntity.ok(new ApiResponse<>("success", "Employees grouped by department", grouped));
    }
}
