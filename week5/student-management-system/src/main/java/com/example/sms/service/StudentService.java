package com.example.sms.service;

import com.example.sms.dto.*;
import java.util.List;

public interface StudentService {
    StudentResponseDTO addStudent(StudentRequestDTO dto);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO getStudentById(Long id);
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);
    void deleteStudent(Long id);
}