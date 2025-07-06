package com.example.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Positive(message = "Age must be positive")
    private int age;

    @Pattern(
        regexp = "^(A\\+|A\\-|A|B\\+|B\\-|B|C\\+|C\\-|C)$",
        message = "Grade format invalid (e.g., A+, B, C-)"
    )
    private String grade;

    private String address;
}