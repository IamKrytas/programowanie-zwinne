package com.project.repository;

import com.project.model.Student;
import com.project.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByEmail(String email);
}
