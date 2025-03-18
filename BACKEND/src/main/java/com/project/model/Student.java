package com.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "student")
public class Student {

    @Id
    private String id;

    @NotBlank(message = "Imię nie może być puste!")
    @Size(min = 2, max = 30, message = "Imię musi zawierać od {min} do {max} znaków!")
    @Field(name = "imie")
    private String imie;

    @NotBlank(message = "Nazwisko nie może być puste!")
    @Size(min = 2, max = 50, message = "Nazwisko musi zawierać od {min} do {max} znaków!")
    @Field(name = "nazwisko")
    private String nazwisko;

    @NotBlank(message = "Numer indeksu nie może być pusty!")
    @Size(min = 6, max = 10, message = "Numer indeksu musi mieć od {min} do {max} znaków!")
    @Field(name = "nr_indeksu")
    private String nrIndeksu;

    @Field(name = "email")
    private String email;

    @Field(name = "stacjonarny")
    private Boolean stacjonarny;

    @CreatedDate
    @Field(name = "dataczas_utworzenia")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "dataczas_modyfikacji")
    private LocalDateTime lastModifiedDate;

    private Set<String> projektyIds;

    public Student() {}

    public Student(String imie, String nazwisko, String nrIndeksu, Boolean stacjonarny) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.stacjonarny = stacjonarny;
    }

    public Student(String imie, String nazwisko, String nrIndeksu, String email, Boolean stacjonarny) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.email = email;
        this.stacjonarny = stacjonarny;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getNrIndeksu() { return nrIndeksu; }
    public void setNrIndeksu(String nrIndeksu) { this.nrIndeksu = nrIndeksu; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getStacjonarny() { return stacjonarny; }
    public void setStacjonarny(Boolean stacjonarny) { this.stacjonarny = stacjonarny; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public Set<String> getProjektyIds() { return projektyIds; }
    public void setProjektyIds(Set<String> projektyIds) { this.projektyIds = projektyIds; }

    public String getStudentId() {
        return this.id;
    }
}
