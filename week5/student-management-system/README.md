# 🎓 Student Management System

A robust Spring Boot application that performs advanced CRUD operations on student records, complete with validation, error handling, MySQL database integration, and REST APIs. It follows clean architecture principles and includes unit/integration testing and Swagger API documentation.

## 🚀 Features

- ✅ Add, update, delete, and retrieve student records
- ✅ Input validation (e.g. name not blank, age positive, grade format)
- ✅ Global exception handling with custom errors
- ✅ DTO-based request/response structure
- ✅ MySQL Database Integration (JPA + Hibernate)
- ✅ Swagger/OpenAPI UI for API testing
- ✅ Unit tests (JUnit + Mockito)
- ✅ Integration tests (Testcontainers + MySQL)
- ✅ Lombok to reduce boilerplate

## 🧱 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Web + Spring Data JPA**
- **MySQL 8+**
- **Hibernate Validator**
- **Lombok**
- **Swagger/OpenAPI (springdoc)**
- **JUnit 5 + Mockito**
- **Testcontainers**
- **Maven**

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/sms/
│   │   ├── controller/
│   │   ├── service/impl/
│   │   ├── service/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── config/
│   └── resources/
│       └── application.properties
└── test/java/com/example/sms/
    ├── controller/
    ├── service/
    └── integration/
```

## ⚙️ Setup Instructions

### 1. ✅ Prerequisites

- Java 17+
- Maven
- MySQL 8+
- Docker (for integration tests)
- Postman or Swagger UI

### 2. 📦 Create Database

Create a MySQL database named:

```sql
CREATE DATABASE student_db;
```

*Optionally, match the DB name from your .env or application.properties.*

### 3. 🧪 Configure

**application.properties**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Swagger config (optional)
springdoc.api-docs.path=/api-docs
```

### 4. 🚀 Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Endpoints

| **Method** | **Endpoint** | **Description** |
|------------|--------------|-----------------|
| POST | `/students` | Add a new student |
| GET | `/students` | Get all students |
| GET | `/students/{id}` | Get student by ID |
| PUT | `/students/{id}` | Update student by ID |
| DELETE | `/students/{id}` | Delete student by ID |

## 📘 Swagger UI

Access interactive API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Running Tests

### 🧪 Unit Tests

```bash
mvn test
```

### 🧪 Integration Tests with Testcontainers

*Requires Docker to be running*

```bash
mvn verify
```

## ✅ Example Request (POST)

```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Arya Stark",
        "age": 18,
        "grade": "A+",
        "address": "Winterfell"
      }'
```

## 📋 Example Response

```json
{
  "id": 1,
  "name": "Arya Stark",
  "age": 18,
  "grade": "A+",
  "address": "Winterfell",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

## 🔧 Development

### Adding New Features

1. Create/update DTOs in `dto/` package
2. Update entity models in `model/` package
3. Add service methods in `service/` package
4. Implement in service implementation `service/impl/`
5. Add controller endpoints in `controller/` package
6. Write unit and integration tests

### Error Handling

The application includes global exception handling for:
- Validation errors (400 Bad Request)
- Resource not found (404 Not Found)
- Internal server errors (500 Internal Server Error)

## 📸 Screenshots

### Swagger Menu
![Main Menu](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/MainMenu.png?raw=true)
*Application's main menu showing available options*

### GET Student
![GET Student](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/AddStudent.png?raw=true)
*Form for adding a new student record*

### Before PUT
![Before](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/ViewAllStudents.png?raw=true)
*Display of all student records in the system*

### After PUT
![Updated](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/UpdateStudent.png?raw=true)
*Student record update interface*

### POST
![Post](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/CSVExport.png?raw=true)

### DELETE
![Delete](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week4/StudentManagementSystem/Screenshots/JunitTest.png?raw=true)