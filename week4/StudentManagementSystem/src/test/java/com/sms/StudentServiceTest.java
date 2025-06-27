package com.sms;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {
    
    @Mock
    private StudentDao studentDao;
    
    @InjectMocks
    private StudentService studentService;
    
    private Student validStudent;
    private Student invalidStudent;

    @Before
    public void setUp() {
        validStudent = new Student(1, "Test Student", "test@example.com", 20, "Computer Science");
        invalidStudent = new Student(0, "Test", "invalid-email", 15, "");
    }

    @Test
    public void testAddValidStudent() {
        when(studentDao.addStudent(any(Student.class))).thenReturn(1);
        
        int id = studentService.addStudent(validStudent);
        assertEquals(1, id);
        verify(studentDao).addStudent(validStudent);
    }

    @Test(expected = SMSException.class)
    public void testAddStudentWithInvalidEmail() {
        studentService.addStudent(invalidStudent);
    }

    @Test(expected = SMSException.class)
    public void testAddStudentWithInvalidAge() {
        Student student = new Student(0, "Test", "valid@email.com", 0, "Valid Dept");
        studentService.addStudent(student);
    }

    @Test
    public void testGetAllStudents() {
        List<Student> mockStudents = Arrays.asList(
            new Student(1, "Student 1", "s1@example.com", 20, "CS"),
            new Student(2, "Student 2", "s2@example.com", 21, "Math")
        );
        when(studentDao.getAllStudents()).thenReturn(mockStudents);
        
        List<Student> students = studentService.getAllStudents();
        assertEquals(2, students.size());
        verify(studentDao).getAllStudents();
    }

    @Test
    public void testGetExistingStudent() {
        when(studentDao.getStudent(1)).thenReturn(Optional.of(validStudent));
        
        Optional<Student> student = studentService.getStudent(1);
        assertTrue(student.isPresent());
        assertEquals("Test Student", student.get().getName());
        verify(studentDao).getStudent(1);
    }

    @Test
    public void testGetNonExistingStudent() {
        when(studentDao.getStudent(99)).thenReturn(Optional.empty());
        
        Optional<Student> student = studentService.getStudent(99);
        assertFalse(student.isPresent());
        verify(studentDao).getStudent(99);
    }

    @Test
    public void testUpdateStudent() {
        when(studentDao.updateStudent(validStudent)).thenReturn(true);
        
        boolean result = studentService.updateStudent(validStudent);
        assertTrue(result);
        verify(studentDao).updateStudent(validStudent);
    }

    @Test
    public void testDeleteStudent() {
        when(studentDao.deleteStudent(1)).thenReturn(true);
        
        boolean result = studentService.deleteStudent(1);
        assertTrue(result);
        verify(studentDao).deleteStudent(1);
    }

    @Test
    public void testSearchStudents() {
        List<Student> mockStudents = Arrays.asList(
            new Student(1, "Computer Science Student", "cs@example.com", 20, "CS"),
            new Student(2, "Math Student", "math@example.com", 21, "Math"),
            new Student(3, "Another CS Student", "another@example.com", 22, "Computer Science")
        );
        when(studentDao.getAllStudents()).thenReturn(mockStudents);
        
        List<Student> results = studentService.searchStudents("computer");
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(s -> s.getName().contains("Computer Science")));
        assertTrue(results.stream().anyMatch(s -> s.getDepartment().equals("Computer Science")));
    }

    @Test(expected = SMSException.class)
    public void testUpdateStudentWithInvalidData() {
        studentService.updateStudent(invalidStudent);
    }
}