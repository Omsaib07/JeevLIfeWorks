package com.example.sms.service.impl;

import com.example.sms.dto.*;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.model.Student;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    private StudentResponseDTO mapToDTO(Student entity) {
        return StudentResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .grade(entity.getGrade())
                .address(entity.getAddress())
                .build();
    }

    private Student mapToEntity(StudentRequestDTO dto) {
        return Student.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .grade(dto.getGrade())
                .address(dto.getAddress())
                .build();
    }

    @Override
    public StudentResponseDTO addStudent(StudentRequestDTO dto) {
        Student saved = repo.save(mapToEntity(dto));
        log.info("Student created with id {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapToDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setAddress(dto.getAddress());
        return mapToDTO(repo.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Student", "id", id);
        }
        repo.deleteById(id);
        log.info("Student with id {} deleted", id);
    }
}