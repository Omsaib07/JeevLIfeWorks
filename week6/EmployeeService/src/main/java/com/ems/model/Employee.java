package com.ems.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an employee in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Employee {

    private int id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Department is mandatory")
    private String department;

    @NotBlank(message = "Position is mandatory")
    private String position;

    @Min(value = 1000, message = "Salary must be at least 1000")
    private double salary;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}
