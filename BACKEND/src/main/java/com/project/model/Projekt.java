package com.projepackage com.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "projekty") // Odpowiada nazwie kolekcji w MongoDB
public class Projekt {

    @Id
    private String id;  // W MongoDB klucz główny to String (ObjectId)

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    @Field(name = "nazwa") // Odpowiada nazwie pola w MongoDB
    private String nazwa;

    @CreatedDate
    @Field(name = "dataczas_utworzenia")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "dataczas_modyfikacji")
    private LocalDateTime lastModifiedDate;

    @JsonIgnoreProperties({"projekt"})
    private List<Zadanie> zadania;

    private Set<Student> studenci;

    // ✅ Pusty konstruktor (wymagany przez Spring Data)
    public Projekt() {}

    // ✅ Konstruktor z nazwą
    public Projekt(String nazwa) {
        this.nazwa = nazwa;
    }

    // ✅ Gettery i Settery
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public List<Zadanie> getZadania() { return zadania; }
    public void setZadania(List<Zadanie> zadania) { this.zadania = zadania; }

    public Set<Student> getStudenci() { return studenci; }
    public void setStudenci(Set<Student> studenci) { this.studenci = studenci; }
}

