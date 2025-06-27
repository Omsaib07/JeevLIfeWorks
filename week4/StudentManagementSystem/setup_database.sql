-- Create database
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add some sample data
INSERT INTO students (name, email, age, department) VALUES 
('John Doe', 'john.doe@example.com', 20, 'Computer Science'),
('Jane Smith', 'jane.smith@example.com', 21, 'Mathematics'),
('Robert Johnson', 'robert.j@example.com', 22, 'Physics'),
('Emily Davis', 'emily.davis@example.com', 19, 'Biology'),
('Michael Brown', 'michael.b@example.com', 20, 'Chemistry');