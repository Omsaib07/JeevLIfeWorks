package com.ems.service;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ems.exception.EmployeeNotFoundException;
import com.ems.model.Employee;
import com.ems.repositoy.EmployeeRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Optional<Employee> getEmployeeById(int id) {
        return repository.findById(id);
    }

    public void updateEmployee(int id, Employee employee) {
        if (!repository.findById(id).isPresent()) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
        employee.setId(id);
        repository.update(employee);
    }

    public void deleteEmployee(int id) {
        if (!repository.findById(id).isPresent()) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }

    public void addEmployee(Employee employee) {
        if (employee.getSalary() < 1000) {
            throw new IllegalArgumentException("Salary must be at least 1000");
        }
        repository.save(employee);
    }

  
    public List<Employee> filterHighSalary(double threshold) {
        return getAllEmployees().stream()
                .filter(emp -> emp.getSalary() > threshold)
                .collect(Collectors.toList());
    }

    public Map<String, List<Employee>> groupByDepartment() {
        return getAllEmployees().stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }
}

