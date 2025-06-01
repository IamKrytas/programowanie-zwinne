package com.project.repository;

import com.project.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedStudentId(String studentId, Pageable pageable);
    List<Task> findByTeacherId(String userId, Pageable pageable);
    List<Task> findByTeacherId(String userId);
}
