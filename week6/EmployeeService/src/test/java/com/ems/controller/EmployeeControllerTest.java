package com.ems.controller;

//Importing the Employee model
import com.ems.model.Employee;
//Importing the service that will be mocked
import com.ems.service.EmployeeService;
//Jackson's ObjectMapper used to convert Java objects to JSON and vice versa
import com.fasterxml.jackson.databind.ObjectMapper;

//JUnit 5 annotation for writing test methods
import org.junit.jupiter.api.Test;
//Spring Boot annotation to auto-configure MockMvc for web layer testing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//Annotation to mock service-layer dependencies in Spring Boot tests
import org.springframework.boot.test.mock.mockito.MockBean;
//MediaType defines content types like JSON
import org.springframework.http.MediaType;

//Used for simulating HTTP requests to controller endpoints
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Map;

//Static imports to simplify mock and assert statements
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This annotation initializes only the web layer and loads EmployeeController for testing
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

 // Autowire MockMvc to simulate HTTP requests and test controller endpoints
 @Autowired
 private MockMvc mockMvc;

 // Mock the service layer to isolate controller testing
 @MockBean
 private EmployeeService service;

 // ObjectMapper helps serialize Java objects to JSON for request bodies
 @Autowired
 private ObjectMapper objectMapper;

 // Test case for GET /employees - retrieve all employees
 @Test
 void testGetAllEmployees() throws Exception {
     // Create a sample employee
     Employee emp = new Employee(1, "John", "HR", "Manager", 80000.0, null);
     // Mock the service to return the sample employee
     when(service.getAllEmployees()).thenReturn(List.of(emp));

     // Perform a GET request and verify the response content
     mockMvc.perform(get("/employees"))
             .andExpect(status().isOk()) // Expect HTTP 200 OK
             .andExpect(jsonPath("$.status").value("success")) // Expect status field in JSON to be "success"
             .andExpect(jsonPath("$.data[0].name").value("John")); // Expect first employee's name to be "John"
 }

 // Test case for GET /employees/{id} - retrieve an employee by ID
 @Test
 void testGetEmployeeById() throws Exception {
     // Create a sample employee
     Employee emp = new Employee(1, "John", "HR", "Manager", 80000.0, null);
     // Mock the service to return the employee by ID
     when(service.getEmployeeById(1)).thenReturn(Optional.of(emp));

     // Perform a GET request to /employees/1 and verify the response
     mockMvc.perform(get("/employees/1"))
             .andExpect(status().isOk()) // Expect HTTP 200 OK
             .andExpect(jsonPath("$.data.name").value("John")); // Expect employee's name in JSON to be "John"
 }

 // Test case for POST /employees - create a new employee
 @Test
 void testCreateEmployee() throws Exception {
     // Create a new employee object
     Employee emp = new Employee(0, "Jane", "IT", "Dev", 90000.0, null);
     // Mock the service to do nothing when addEmployee is called
     doNothing().when(service).addEmployee(any());

     // Perform a POST request with JSON body and verify the response
     mockMvc.perform(post("/employees")
                     .contentType(MediaType.APPLICATION_JSON) // Set request content type to JSON
                     .content(objectMapper.writeValueAsString(emp))) // Convert emp object to JSON string
             .andExpect(status().isOk()) // Expect HTTP 200 OK
             .andExpect(jsonPath("$.message").value("Employee created successfully")); // Expect confirmation message
 }

 // Test case for DELETE /employees/{id} - delete an employee
 @Test
 void testDeleteEmployee() throws Exception {
     // Mock the service to do nothing on delete
     doNothing().when(service).deleteEmployee(1);

     // Perform a DELETE request to /employees/1 and verify the response
     mockMvc.perform(delete("/employees/1"))
             .andExpect(status().isOk()) // Expect HTTP 200 OK
             .andExpect(jsonPath("$.message").value("Employee deleted successfully")); // Expect success message
 }
}
