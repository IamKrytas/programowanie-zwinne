package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "teacher_id")
    private String teacherId;

    @NotEmpty(message = "fileIds cannot be empty")
    @ElementCollection
    @CollectionTable(name = "task_file_ids", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "file_id")
    private Set<String> fileIds;

    @NotBlank
    @Column(name = "assigned_student_id", nullable = false)
    private String assignedStudentId;

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Column(name = "description", nullable = false)
    private String description;

    @Min(1)
    @Column(name = "priority")
    private int priority;

    @NotNull
    @Column(name = "done_date")
    private LocalDateTime doneDate;

    @NotNull
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}