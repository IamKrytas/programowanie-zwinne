package com.project.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.model.Student;

public interface StudentService {
    Optional<Student> getStudent(String studentId);
    Student setStudent(Student student);
    void deleteStudent(String studentId);
    Page<Student> getStudenci(Pageable pageable);
    Optional<Student> findByNrIndeksu(String nrIndeksu);
}
