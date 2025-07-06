package com.example.sms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;          // ← add
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor      // ← add
@AllArgsConstructor     // ← add
@Builder                // ← add
public class StudentRequestDTO {

    @NotBlank(message = "Name must not be empty")
    private String name;

    @Min(value = 1, message = "Age must be positive")
    private int age;

    @Pattern(
        regexp = "^(A\\+|A|B\\+|B|C\\+|C|C-)$",
        message = "Grade must be one of: A+, A, B+, B, C+, C, C-"
    )
    private String grade;

    private String address;
}