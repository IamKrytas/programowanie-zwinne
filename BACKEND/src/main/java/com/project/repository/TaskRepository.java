package com.project.repository;

import com.project.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedStudentId(String studentId, Pageable pageable);
    List<Task> findByTeacherId(String userId, Pageable pageable);
    List<Task> findByTeacherId(String userId);
}
