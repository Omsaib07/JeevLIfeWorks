package com.sms;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class StudentManagementApp {
    private static final Logger logger = Logger.getLogger(StudentManagementApp.class.getName());
    private final StudentService studentService;
    private final Scanner scanner;

    public StudentManagementApp(StudentService studentService) {
        this.studentService = studentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Student Management System Started");
        
        while (true) {
            SMSUtil.displayMenu();
            int choice = SMSUtil.getValidatedInt("Enter your choice: ", 1, 7);
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    exportStudentsToCsv();
                    break;
                case 7:
                    System.out.println("Exiting Student Management System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudent() {
        System.out.println("\nAdd New Student");
        Student student = new Student();
        
        student.setName(SMSUtil.getValidatedInput("Enter name: ", 
            name -> !name.trim().isEmpty(), "Name cannot be empty"));
        
        student.setEmail(SMSUtil.getValidatedInput("Enter email: ", 
            SMSUtil::isValidEmail, "Invalid email format"));
        
        student.setAge(SMSUtil.getValidatedInt("Enter age: ", 1, 120));
        
        student.setDepartment(SMSUtil.getValidatedInput("Enter department: ", 
            dept -> !dept.trim().isEmpty(), "Department cannot be empty"));
        
        int id = studentService.addStudent(student);
        System.out.println("Student added successfully with ID: " + id);
    }

    private void viewAllStudents() {
        System.out.println("\nAll Students");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }

    private void viewStudentById() {
        System.out.println("\nView Student by ID");
        int id = SMSUtil.getValidatedInt("Enter student ID: ", 1, Integer.MAX_VALUE);
        Optional<Student> student = studentService.getStudent(id);
        if (student.isPresent()) {
            System.out.println(student.get());
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private void updateStudent() {
        System.out.println("\nUpdate Student");
        int id = SMSUtil.getValidatedInt("Enter student ID to update: ", 1, Integer.MAX_VALUE);
        
        Optional<Student> optionalStudent = studentService.getStudent(id);
        if (!optionalStudent.isPresent()) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }
        
        Student student = optionalStudent.get();
        System.out.println("Current student details: " + student);
        
        System.out.println("Enter new details (leave blank to keep current value):");
        
        String newName = SMSUtil.getValidatedInput("Enter name [" + student.getName() + "]: ", 
            input -> input.isEmpty() || !input.trim().isEmpty(), "Name cannot be empty");
        if (!newName.isEmpty()) student.setName(newName);
        
        String newEmail = SMSUtil.getValidatedInput("Enter email [" + student.getEmail() + "]: ", 
            input -> input.isEmpty() || SMSUtil.isValidEmail(input), "Invalid email format");
        if (!newEmail.isEmpty()) student.setEmail(newEmail);
        
        String ageInput = SMSUtil.getValidatedInput("Enter age [" + student.getAge() + "]: ", 
            input -> input.isEmpty() || (input.matches("\\d+") && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= 120),
            "Age must be between 1 and 120");
        if (!ageInput.isEmpty()) student.setAge(Integer.parseInt(ageInput));
        
        String newDept = SMSUtil.getValidatedInput("Enter department [" + student.getDepartment() + "]: ", 
            input -> input.isEmpty() || !input.trim().isEmpty(), "Department cannot be empty");
        if (!newDept.isEmpty()) student.setDepartment(newDept);
        
        boolean updated = studentService.updateStudent(student);
        if (updated) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Failed to update student.");
        }
    }

    private void deleteStudent() {
        System.out.println("\nDelete Student");
        int id = SMSUtil.getValidatedInt("Enter student ID to delete: ", 1, Integer.MAX_VALUE);
        
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            System.out.println("Student with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Student with ID " + id + " not found or could not be deleted.");
        }
    }

    private void exportStudentsToCsv() {
        System.out.println("\nExporting Students to CSV...");
        studentService.exportStudentsToCsv();
        System.out.println("Students exported to " + DatabaseConfig.getCsvExportPath());
    }

    public static void main(String[] args) {
        try {
            // Initialize DAO and Service
            StudentDao studentDao = new StudentDao();
            StudentService studentService = new StudentService(studentDao);
            
            // Start the application
            StudentManagementApp app = new StudentManagementApp(studentService);
            app.start();
        } catch (Exception e) {
            logger.severe("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}