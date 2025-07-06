package com.example.sms.service;

import com.example.sms.dto.*;
import com.example.sms.model.Student;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository repo;

    private StudentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new StudentServiceImpl(repo);
    }

    @Test
    void addStudent_saves_and_returns_dto() {
        StudentRequestDTO req = StudentRequestDTO.builder()
                .name("Bran")
                .age(15)
                .grade("B+")
                .address("Winterfell")
                .build();

        Student saved = Student.builder().id(1L).name("Bran").age(15).grade("B+").address("Winterfell").build();
        when(repo.save(any(Student.class))).thenReturn(saved);

        StudentResponseDTO res = service.addStudent(req);
        assertThat(res.getId()).isEqualTo(1L);
        verify(repo).save(any(Student.class));
    }
}