package com.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;

public abstract class BaseDatabaseTest {
    protected Connection connection;

    @Before
    public void setUp() throws Exception {
        // Setup H2 in-memory database
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        
        // Drop table if exists
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS students");
            
            // Create fresh table
            stmt.execute("CREATE TABLE students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL, " +
                "department VARCHAR(100) NOT NULL)");
            
            // Insert test data
            stmt.execute("INSERT INTO students (name, email, age, department) VALUES " +
                "('Test Student', 'test@example.com', 20, 'Computer Science')");
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS students");
            }
            connection.close();
        }
    }
}