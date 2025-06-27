# 🎓 Student Management System

A Java-based application for managing student records with MySQL database integration.

## ✨ Features

- ✅ CRUD operations for student records
- ✅ Data validation and error handling
- ✅ CSV export functionality
- ✅ Console-based user interface
- ✅ Comprehensive logging using Log4j2

## 🛠️ Technologies Used

- **Java 8** – Core application logic
- **JDBC** – Database connectivity
- **MySQL** – Relational data storage
- **Log4j2** – Logging framework
- **JUnit 4 & Mockito** – Unit testing
- **H2 Database** – In-memory testing
- **Maven** – Build and dependency management

## 🧱 Architecture

The application follows a **layered architecture**:

```
Presentation Layer (Console UI)
         ↓
Service Layer (Business Logic)
         ↓
DAO Layer (Database Access)
         ↓
    MySQL Database
```

### 🧩 Key Design Patterns

- **DAO Pattern** – Abstracts and encapsulates all access to the data source
- **Service Layer Pattern** – Contains business logic and validation
- **Singleton Pattern** – Used for configuration and database utility classes

## 🚀 Prerequisites

Ensure the following are installed:

- **Java JDK 11+**
- **MySQL Server 8.0+**
- **Maven 3.6+**

## ⚙️ Setup Instructions

### 📌 1. MySQL Database Setup

```sql
-- Login to MySQL
mysql -u root -p

-- Create database and user
CREATE DATABASE student_db;
CREATE USER 'sms_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON student_db.* TO 'sms_user'@'localhost';
FLUSH PRIVILEGES;

-- Switch to the new database
USE student_db;

-- Create students table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 📌 2. Configuration

Edit `src/main/resources/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC
db.username=sms_user
db.password=password
student.csv.export.path=students_export.csv
log.file.path=student_management.log
```

### 📌 3. Build and Run

```bash
# Build the project
mvn clean package

# Run the application using Maven
mvn exec:java -Dexec.mainClass="com.sms.StudentManagementApp"

# Or run the JAR directly
java -jar target/student-management-system-1.0-SNAPSHOT.jar
```

### 📌 4. Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn -Dtest=StudentServiceTest test

# Generate code coverage report
mvn jacoco:report
```

## 📸 Screenshots

### Main Menu
![Main Menu](screenshots/main-menu.png)
*Application's main menu showing available options*

### Add Student
![Add Student](screenshots/add-student.png)
*Form for adding a new student record*

### View All Students
![View All Students](screenshots/view-students.png)
*Display of all student records in the system*

### Update Student
![Update Student](screenshots/update-student.png)
*Student record update interface*

### CSV Export
![CSV Export](screenshots/csv-export.png)
*CSV export functionality and success message*

