package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Surname should have at least {min} and maximum of {max} characters.")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotBlank()
    @Size(min = 2, max = 50, message = "E-mail should have at least {min} and maximum of {max} characters.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank()
    @Size(min = 18, max = 500)
    @Column(name = "password", nullable = false)
    private String password;
}