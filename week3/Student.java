import java.io.Serializable;
import java.util.Objects;

/**
 * Student class representing a student entity with basic information
 * Implements Serializable for file I/O operations and Comparable for sorting
 */
public class Student implements Serializable, Comparable<Student> {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private int age;
    private String grade;
    private String address;
    
    /**
     * Default constructor
     */
    public Student() {
    }
    
    /**
     * Parameterized constructor
     * @param id Student ID
     * @param name Student name
     * @param age Student age
     * @param grade Student grade
     * @param address Student address
     */
    public Student(int id, String name, int age, String grade, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.address = address;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public String getAddress() {
        return address;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * String representation of the student
     * @return Formatted string with student details
     */
    @Override
    public String toString() {
        return String.format("Student{ID: %d, Name: '%s', Age: %d, Grade: '%s', Address: '%s'}", 
                           id, name, age, grade, address);
    }
    
    /**
     * Equals method to compare students based on ID
     * @param o Object to compare
     * @return true if students have same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }
    
    /**
     * HashCode method based on ID
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * CompareTo method for sorting students by name, then by ID
     * @param other Student to compare with
     * @return comparison result
     */
    @Override
    public int compareTo(Student other) {
        int nameComparison = this.name.compareToIgnoreCase(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return Integer.compare(this.id, other.id);
    }
}