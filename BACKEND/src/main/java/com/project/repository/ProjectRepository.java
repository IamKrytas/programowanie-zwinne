package com.project.repository;

import com.project.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTeacherId(String teacherId);
    List<Project> findByTeacherId(String teacherId, Pageable pageable);
    
    @Query("SELECT p FROM Project p JOIN p.studentIds s WHERE s = :studentId")
    List<Project> findByStudentIdsContaining(@Param("studentId") String studentId, Pageable pageable);
    
    Optional<Project> findByIdAndTeacherId(Long projectId, String teacherId);
    
    @Query("SELECT p FROM Project p JOIN p.studentIds s WHERE p.id = :projectId AND s = :studentId")
    Optional<Project> findByIdAndStudentIdsContaining(@Param("projectId") Long projectId, @Param("studentId") String studentId);
}

