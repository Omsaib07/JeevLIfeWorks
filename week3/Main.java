import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class for the Student Management System
 * Provides a menu-driven console interface for managing students
 */
public class Main {
    private static StudentManager manager = new StudentManager();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("    STUDENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        
        // Load existing data
        manager.loadFromFile();
        
        // Main program loop
        while (true) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice (1-6): ");
                
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        removeStudent();
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        searchStudent();
                        break;
                    case 5:
                        displayAllStudents();
                        break;
                    case 6:
                        exitProgram();
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter 1-6.");
                }
                
                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }
    
    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Add a new student");
        System.out.println("2. Remove a student by ID");
        System.out.println("3. Update student details by ID");
        System.out.println("4. Search for a student by ID");
        System.out.println("5. Display all students (sorted)");
        System.out.println("6. Exit and save data");
        System.out.println("=".repeat(50));
        System.out.println("Current students in system: " + manager.getStudentCount());
        System.out.println("=".repeat(50));
    }
    
    /**
     * Handles adding a new student
     */
    private static void addStudent() {
        System.out.println("\n--- ADD NEW STUDENT ---");
        
        try {
            int id = getIntInput("Enter Student ID: ");
            
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            
            int age = getIntInput("Enter Student Age: ");
            
            System.out.print("Enter Student Grade: ");
            String grade = scanner.nextLine().trim();
            
            System.out.print("Enter Student Address: ");
            String address = scanner.nextLine().trim();
            
            // Validate input data
            StudentManager.validateStudentData(id, name, age, grade, address);
            
            // Create and add student
            Student student = new Student(id, name, age, grade, address);
            manager.addStudent(student);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }
    
    /**
     * Handles removing a student
     */
    private static void removeStudent() {
        System.out.println("\n--- REMOVE STUDENT ---");
        
        try {
            int id = getIntInput("Enter Student ID to remove: ");
            
            // First, show the student details
            Student student = manager.searchStudent(id);
            if (student != null) {
                System.out.println("Student found: " + student);
                System.out.print("Are you sure you want to remove this student? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y") || confirm.equals("yes")) {
                    manager.removeStudent(id);
                } else {
                    System.out.println("Remove operation cancelled.");
                }
            } else {
                System.out.println("Student with ID " + id + " not found!");
            }
            
        } catch (Exception e) {
            System.out.println("Error removing student: " + e.getMessage());
        }
    }
    
    /**
     * Handles updating a student
     */
    private static void updateStudent() {
        System.out.println("\n--- UPDATE STUDENT ---");
        
        try {
            int id = getIntInput("Enter Student ID to update: ");
            
            // First, show current student details
            Student student = manager.searchStudent(id);
            if (student == null) {
                System.out.println("Student with ID " + id + " not found!");
                return;
            }
            
            System.out.println("Current details: " + student);
            System.out.println("Enter new details (press Enter to keep current value):");
            
            System.out.print("Enter new name [" + student.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = student.getName();
            
            System.out.print("Enter new age [" + student.getAge() + "]: ");
            String ageStr = scanner.nextLine().trim();
            int age = ageStr.isEmpty() ? student.getAge() : Integer.parseInt(ageStr);
            
            System.out.print("Enter new grade [" + student.getGrade() + "]: ");
            String grade = scanner.nextLine().trim();
            if (grade.isEmpty()) grade = student.getGrade();
            
            System.out.print("Enter new address [" + student.getAddress() + "]: ");
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) address = student.getAddress();
            
            // Validate input data
            StudentManager.validateStudentData(id, name, age, grade, address);
            
            // Update student
            manager.updateStudent(id, name, age, grade, address);
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for age.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }
    
    /**
     * Handles searching for a student
     */
    private static void searchStudent() {
        System.out.println("\n--- SEARCH STUDENT ---");
        
        try {
            int id = getIntInput("Enter Student ID to search: ");
            
            Student student = manager.searchStudent(id);
            if (student != null) {
                System.out.println("\nStudent found:");
                System.out.println("-".repeat(40));
                System.out.println(student);
                System.out.println("-".repeat(40));
            } else {
                System.out.println("Student with ID " + id + " not found!");
            }
            
        } catch (Exception e) {
            System.out.println("Error searching student: " + e.getMessage());
        }
    }
    
    /**
     * Handles displaying all students
     */
    private static void displayAllStudents() {
        manager.displayAllStudents();
    }
    
    /**
     * Handles program exit and saves data
     */
    private static void exitProgram() {
        System.out.println("\n--- EXIT PROGRAM ---");
        System.out.println("Saving data...");
        manager.saveToFile();
        System.out.println("Thank you for using Student Management System!");
        System.out.println("Goodbye!");
    }
    
    /**
     * Gets integer input with validation
     * @param prompt Prompt message
     * @return Valid integer input
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}