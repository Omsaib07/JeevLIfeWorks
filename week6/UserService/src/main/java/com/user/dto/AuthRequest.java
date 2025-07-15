package com.user.dto;

//Lombok annotation that automatically generates:
//- Getters and setters
//- toString(), equals(), and hashCode() methods
//- A constructor for all final fields (not used here since fields aren't final)
import lombok.Data;

//Marks this class as a simple data holder for authentication requests
@Data
public class AuthRequest {

 // Field to hold the username provided during login
 private String username;

 // Field to hold the password provided during login
 private String password;
}
