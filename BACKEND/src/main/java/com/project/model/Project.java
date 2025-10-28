package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "fileIds cannot be empty")
    @ElementCollection
    @CollectionTable(name = "project_file_ids", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "file_id")
    private Set<String> fileIds;

    @NotBlank(message = "teacherId cannot be blank")
    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @NotEmpty(message = "tasks cannot be empty")
    @ElementCollection
    @CollectionTable(name = "project_task_ids", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "task_id")
    private Set<String> taskIds;

    @NotEmpty(message = "students cannot be empty")
    @ElementCollection
    @CollectionTable(name = "project_student_ids", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "student_id")
    private Set<String> studentIds;

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "done_date")
    private LocalDateTime doneDate;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
