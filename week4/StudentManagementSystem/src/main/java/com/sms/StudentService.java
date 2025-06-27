package com.sms;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StudentService {
    private static final Logger logger = Logger.getLogger(StudentService.class.getName());
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public int addStudent(Student student) {
        validateStudent(student);
        return studentDao.addStudent(student);
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public Optional<Student> getStudent(int id) {
        return studentDao.getStudent(id);
    }

    public boolean updateStudent(Student student) {
        validateStudent(student);
        return studentDao.updateStudent(student);
    }

    public boolean deleteStudent(int id) {
        return studentDao.deleteStudent(id);
    }

    public void exportStudentsToCsv() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            logger.info("No students to export");
            return;
        }

        String csvPath = DatabaseConfig.getCsvExportPath();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath))) {
            // Write header
            writer.write("ID,Name,Email,Age,Department\n");
            
            // Write data
            for (Student student : students) {
                writer.write(String.format("%d,%s,%s,%d,%s\n",
                        student.getId(),
                        escapeCsv(student.getName()),
                        escapeCsv(student.getEmail()),
                        student.getAge(),
                        escapeCsv(student.getDepartment())));
            }
            logger.info("Successfully exported " + students.size() + " students to " + csvPath);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error exporting students to CSV", ex);
            throw new SMSException("Error exporting students to CSV", ex);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.contains(",") ? "\"" + value + "\"" : value;
    }

    private void validateStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new SMSException("Student name cannot be empty");
        }
        if (student.getEmail() == null || !SMSUtil.isValidEmail(student.getEmail())) {
            throw new SMSException("Invalid email format");
        }
        if (student.getAge() <= 0 || student.getAge() > 120) {
            throw new SMSException("Age must be between 1 and 120");
        }
        if (student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            throw new SMSException("Department cannot be empty");
        }
    }

    public List<Student> searchStudents(String searchTerm) {
        return getAllStudents().stream()
                .filter(s -> s.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        s.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        s.getDepartment().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}