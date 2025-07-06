package com.example.sms.integration;

import com.example.sms.model.Student;
import com.example.sms.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
class StudentIntegrationTest {

    @Container
    static MySQLContainer<?> db = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("student_db")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.username", db::getUsername);
        registry.add("spring.datasource.password", db::getPassword);
    }

    @Autowired
    private StudentRepository repo;

    @Test
    void repository_saves_student() {
        Student s = Student.builder().name("Sam").age(22).grade("B").build();
        Student saved = repo.save(s);
        assertThat(saved.getId()).isNotNull();
    }
}