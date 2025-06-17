import java.io.*;
import java.util.*;

/**
 * StudentManager class to manage student operations
 * Uses ArrayList, HashMap, and TreeSet for efficient data management
 */
public class StudentManager {
    private ArrayList<Student> students;
    private HashMap<Integer, Student> studentMap;
    private TreeSet<Student> sortedStudents;
    private static final String DEFAULT_FILE = "students.dat";
    
    /**
     * Constructor initializes all data structures
     */
    public StudentManager() {
        students = new ArrayList<>();
        studentMap = new HashMap<>();
        sortedStudents = new TreeSet<>();
    }
    
    /**
     * Adds a new student to all data structures
     * @param student Student object to add
     * @throws IllegalArgumentException if student ID already exists
     */
    public void addStudent(Student student) throws IllegalArgumentException {
        if (studentMap.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists!");
        }
        
        students.add(student);
        studentMap.put(student.getId(), student);
        sortedStudents.add(student);
        
        System.out.println("Student added successfully!");
    }
    
    /**
     * Removes a student by ID from all data structures
     * @param id Student ID to remove
     * @return true if student was removed, false if not found
     */
    public boolean removeStudent(int id) {
        Student student = studentMap.get(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found!");
            return false;
        }
        
        students.remove(student);
        studentMap.remove(id);
        sortedStudents.remove(student);
        
        System.out.println("Student removed successfully!");
        return true;
    }
    
    /**
     * Updates student information
     * @param id Student ID to update
     * @param name New name
     * @param age New age
     * @param grade New grade
     * @param address New address
     * @return true if updated successfully, false if student not found
     */
    public boolean updateStudent(int id, String name, int age, String grade, String address) {
        Student student = studentMap.get(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found!");
            return false;
        }
        
        // Remove from TreeSet before updating (to maintain sorting)
        sortedStudents.remove(student);
        
        // Update student information
        student.setName(name);
        student.setAge(age);
        student.setGrade(grade);
        student.setAddress(address);
        
        // Add back to TreeSet with updated information
        sortedStudents.add(student);
        
        System.out.println("Student updated successfully!");
        return true;
    }
    
    /**
     * Searches for a student by ID
     * @param id Student ID to search
     * @return Student object if found, null otherwise
     */
    public Student searchStudent(int id) {
        return studentMap.get(id);
    }
    
    /**
     * Displays all students in sorted order (by name, then by ID)
     */
    public void displayAllStudents() {
        if (sortedStudents.isEmpty()) {
            System.out.println("No students found!");
            return;
        }
        
        System.out.println("\n=== ALL STUDENTS (Sorted by Name) ===");
        System.out.println("Total Students: " + sortedStudents.size());
        System.out.println("-".repeat(60));
        
        for (Student student : sortedStudents) {
            System.out.println(student);
        }
        System.out.println("-".repeat(60));
    }
    
    /**
     * Loads student data from file
     * @param filename File to load from
     */
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            @SuppressWarnings("unchecked")
            ArrayList<Student> loadedStudents = (ArrayList<Student>) ois.readObject();
            
            // Clear existing data
            students.clear();
            studentMap.clear();
            sortedStudents.clear();
            
            // Add loaded students to all data structures
            for (Student student : loadedStudents) {
                students.add(student);
                studentMap.put(student.getId(), student);
                sortedStudents.add(student);
            }
            
            System.out.println("Data loaded successfully from " + filename);
            System.out.println("Loaded " + students.size() + " students.");
            
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found. Starting with empty data.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Invalid file format. " + e.getMessage());
        }
    }
    
    /**
     * Saves student data to file
     * @param filename File to save to
     */
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
            System.out.println("Data saved successfully to " + filename);
            System.out.println("Saved " + students.size() + " students.");
            
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    /**
     * Loads data from default file
     */
    public void loadFromFile() {
        loadFromFile(DEFAULT_FILE);
    }
    
    /**
     * Saves data to default file
     */
    public void saveToFile() {
        saveToFile(DEFAULT_FILE);
    }
    
    /**
     * Gets the total number of students
     * @return Number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Validates student data
     * @param id Student ID
     * @param name Student name
     * @param age Student age
     * @param grade Student grade
     * @param address Student address
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateStudentData(int id, String name, int age, String grade, String address) 
            throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty!");
        }
        if (age <= 0 || age > 150) {
            throw new IllegalArgumentException("Student age must be between 1 and 150!");
        }
        if (grade == null || grade.trim().isEmpty()) {
            throw new IllegalArgumentException("Student grade cannot be empty!");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Student address cannot be empty!");
        }
    }
}