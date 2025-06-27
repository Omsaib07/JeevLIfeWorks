package com.sms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDao {
    private static final Logger logger = Logger.getLogger(StudentDao.class.getName());
    private static final String INSERT_SQL = "INSERT INTO students(name, email, age, department) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM students";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM students WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE students SET name = ?, email = ?, age = ?, department = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM students WHERE id = ?";

    // Static block to ensure driver registration
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("MySQL JDBC Driver successfully registered");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to register MySQL JDBC Driver", e);
            throw new SMSException("MySQL JDBC Driver not found", e);
        }
    }

    public Optional<Student> getStudent(int id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudent(rs));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error retrieving student with ID: " + id, ex);
            throw new SMSException("Error retrieving student", ex);
        }
        return Optional.empty();
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error retrieving all students", ex);
            throw new SMSException("Error retrieving students", ex);
        }
        return students;
    }

    public int addStudent(Student student) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setStudentParameters(stmt, student);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SMSException("Creating student failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SMSException("Creating student failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error adding student: " + student, ex);
            throw new SMSException("Error adding student", ex);
        }
    }

    public boolean updateStudent(Student student) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            setStudentParameters(stmt, student);
            stmt.setInt(5, student.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error updating student: " + student, ex);
            throw new SMSException("Error updating student", ex);
        }
    }

    public boolean deleteStudent(int id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error deleting student with ID: " + id, ex);
            throw new SMSException("Error deleting student", ex);
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseConfig.getDbUrl(),
                    DatabaseConfig.getDbUsername(),
                    DatabaseConfig.getDbPassword());
            
            // Test the connection
            if (conn.isValid(2)) { // 2 second timeout
                logger.info("Database connection established successfully");
                return conn;
            } else {
                throw new SQLException("Failed to establish valid database connection");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error connecting to database", ex);
            throw new SMSException("Database connection failed", ex);
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setAge(rs.getInt("age"));
        student.setDepartment(rs.getString("department"));
        return student;
    }

    private void setStudentParameters(PreparedStatement stmt, Student student) throws SQLException {
        stmt.setString(1, student.getName());
        stmt.setString(2, student.getEmail());
        stmt.setInt(3, student.getAge());
        stmt.setString(4, student.getDepartment());
    }
}