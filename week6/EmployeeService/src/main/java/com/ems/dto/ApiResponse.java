package com.ems.dto;

//Lombok annotation to generate getters, setters, toString(), equals(), and hashCode() methods
import lombok.Data;
//Lombok annotation to generate a no-argument constructor
import lombok.NoArgsConstructor;
//Lombok annotation to generate an all-arguments constructor
import lombok.AllArgsConstructor;

//Lombok annotation that combines @Getter, @Setter, @ToString, @EqualsAndHashCode
@Data
//Generates a no-argument constructor automatically
@NoArgsConstructor
//Generates a constructor with all fields as arguments
@AllArgsConstructor
public class ApiResponse<T> {

 // Field to represent the status of the response (e.g., "success", "error")
 private String status;

 // A human-readable message describing the response result
 private String message;

 // The actual data payload returned in the response (generic type T)
 private T data;
}
