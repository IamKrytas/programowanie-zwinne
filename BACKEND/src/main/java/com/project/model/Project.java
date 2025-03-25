package com.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "project")
public class Project {

    @Id
    private int id;

    @NotBlank()
    @Field(name = "fileId")
    private Set<Integer> fileId;

    @NotBlank()
    @Field(name = "teacherId")
    private int teacherId;

    @NotBlank()
    @Field(name = "tasks")
    private Set<Integer> tasks;

    @NotBlank()
    @Field(name = "students")
    private Set<Integer> students;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank()
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Field(name = "description")
    private String description;

    @CreatedDate
    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @CreatedDate
    @Field(name = "creationDate")
    private LocalDateTime creationDate;

}