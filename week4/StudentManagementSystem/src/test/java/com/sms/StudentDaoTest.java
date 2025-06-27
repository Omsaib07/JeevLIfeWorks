package com.sms;

import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StudentDaoTest extends BaseDatabaseTest {
    private StudentDao studentDao;
    private int testStudentId;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        studentDao = new StudentDao();
        
        // Insert a fresh test student for each test
        Student testStudent = new Student(0, "Test Student", "test@example.com", 20, "Computer Science");
        testStudentId = studentDao.addStudent(testStudent);
    }

    @Test
    public void testGetStudent() {
        Optional<Student> student = studentDao.getStudent(testStudentId);
        assertTrue("Student should exist", student.isPresent());
        assertEquals("Test Student", student.get().getName());
        assertEquals("test@example.com", student.get().getEmail());
    }

    @Test
    public void testGetAllStudents() {
        List<Student> students = studentDao.getAllStudents();
        assertFalse("Students list should not be empty", students.isEmpty());
        assertTrue("Should contain our test student", 
            students.stream().anyMatch(s -> s.getId() == testStudentId));
    }

    @Test
    public void testAddStudent() {
        Student newStudent = new Student(0, "New Student", "new@example.com", 21, "Math");
        int newId = studentDao.addStudent(newStudent);
        
        assertTrue("New student ID should be positive", newId > 0);
        Optional<Student> retrieved = studentDao.getStudent(newId);
        assertTrue("Should retrieve the new student", retrieved.isPresent());
        assertEquals("New Student", retrieved.get().getName());
    }

    @Test
    public void testUpdateStudent() {
        Student updatedStudent = new Student(testStudentId, "Updated Name", "updated@example.com", 22, "Physics");
        boolean result = studentDao.updateStudent(updatedStudent);
        
        assertTrue("Update should succeed", result);
        Optional<Student> retrieved = studentDao.getStudent(testStudentId);
        assertTrue("Should retrieve updated student", retrieved.isPresent());
        assertEquals("Updated Name", retrieved.get().getName());
    }

    @Test
    public void testDeleteStudent() {
        boolean result = studentDao.deleteStudent(testStudentId);
        assertTrue("Delete should succeed", result);
        
        Optional<Student> student = studentDao.getStudent(testStudentId);
        assertFalse("Student should be deleted", student.isPresent());
    }
}