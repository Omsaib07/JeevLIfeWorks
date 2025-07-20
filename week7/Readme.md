# 📘 Employee Management System

A simple RESTful Employee Management API built with Spring Boot that supports user authentication and role-based access using Spring Security.

## 📦 Features

* ✅ Add, Get, Update, Delete Employees (CRUD)
* 🔐 Basic Authentication with Role-based Access Control
* 💾 In-memory H2 or MySQL database support
* 🌐 RESTful API endpoints
* 🧪 Tested with Postman

## 🛠️ Tech Stack

* Java 17+
* Spring Boot 3.x
* Spring Security
* Spring Data JPA
* H2 / MySQL (configurable)
* Maven
* Postman

## 🚀 Getting Started

### 🖥️ Clone the repository

```bash
git clone https://github.com/your-username/employee-management-system.git
cd employee-management-system
```

### 📄 Create the directory structure (if needed manually)

### ⚙️ Configure Database

In `src/main/resources/application.properties`:

```properties
# Use H2 (in-memory) or configure MySQL
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Show SQL in console
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

## 🔐 Default In-Memory Users (Basic Auth)

| **Username** | **Password** | **Role** |
|--------------|--------------|----------|
| admin        | admin123     | ADMIN    |
| user         | user123      | USER     |

## 🌐 API Endpoints

| **Method** | **Endpoint**           | **Description**      | **Access**  |
|------------|------------------------|----------------------|-------------|
| GET        | /api/employees         | Get all employees    | ADMIN/USER  |
| POST       | /api/employees         | Add a new employee   | ADMIN       |
| PUT        | /api/employees/{id}    | Update employee      | ADMIN       |
| DELETE     | /api/employees/{id}    | Delete employee      | ADMIN       |

## 🧪 Using Postman

1. **Import the Postman Collection** (shared in previous message)
2. Use Basic Auth: `admin` / `admin123`
3. For POST/PUT, use raw JSON with Content-Type: `application/json`

## 📝 Sample JSON for Employee

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "IT",
  "salary": 50000.0
}
```

## 🏃‍♂️ Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📊 H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)
## Screenshots

### GET
![Post](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week7/Screenshots/GET.png?raw=true)

### PUT
![Delete](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week7/Screenshots/PUT.png?raw=true)
### POST
![Post](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week7/Screenshots/POST.png?raw=true)

### DELETE
![Delete](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week7/Screenshots/DELETE.png?raw=true)

## 🔧 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/employeemanagement/
│   │       ├── EmployeeManagementApplication.java
│   │       ├── controller/
│   │       │   └── EmployeeController.java
│   │       ├── entity/
│   │       │   └── Employee.java
│   │       ├── repository/
│   │       │   └── EmployeeRepository.java
│   │       ├── service/
│   │       │   ├── EmployeeService.java
│   │       │   └── EmployeeServiceImpl.java
│   │       └── config/
│   │           └── SecurityConfig.java
│   └── resources/
│       └── application.properties
└── test/
```

## 🚀 Quick Test Commands

### Get All Employees
```bash
curl -u admin:admin123 http://localhost:8080/api/employees
```

### Add New Employee
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Smith","email":"jane.smith@example.com","department":"HR","salary":45000}'
```

