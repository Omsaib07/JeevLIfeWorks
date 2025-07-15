# Employee Management System (Spring Boot Microservices)

## Microservices
- **User Service** (Port: 8081): Registration, Login, JWT
- **Employee Service** (Port: 8082): CRUD using JDBC, Secured by JWT

## Setup
1. Create `jeevlifedb` MySQL databases.
2. Update `application.properties` with your DB credentials.
3. Run schema.sql in each DB.
4. Start both services using Maven or your IDE.
