package com.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

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

    // Relacje - opcja 1 (przechowujemy tylko ID jako String, zalecane)
    private List<String> zadaniaIds; // ID zadań w MongoDB
    private Set<String> studenciIds; // ID studentów w MongoDB

    // Relacje - opcja 2 (pełne obiekty, jeśli konieczne)
    // @DBRef
    // private List<Zadanie> zadania;

    // @DBRef
    // private Set<Student> studenci;

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

    public List<String> getZadaniaIds() { return zadaniaIds; }
    public void setZadaniaIds(List<String> zadaniaIds) { this.zadaniaIds = zadaniaIds; }

    public Set<String> getStudenciIds() { return studenciIds; }
    public void setStudenciIds(Set<String> studenciIds) { this.studenciIds = studenciIds; }

    // Dodanie metody getProjektId(), odpowiadającej metodzie getId()
    public String getProjektId() {
        return this.id;
    }
}
