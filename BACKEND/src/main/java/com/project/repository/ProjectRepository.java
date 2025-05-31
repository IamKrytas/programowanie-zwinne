package com.project.repository;

import com.project.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByTeacherId(String teacherId, Pageable pageable);

    List<Project> findByStudentIdsContaining(String studentId, Pageable pageable);
    
    Optional<Project> findByIdAndTeacherId(String projectId, String teacherId);

    Optional<Project> findByIdAndStudentIdsContaining(String projectId, String studentId);
}

