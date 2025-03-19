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

@Document(collection = "zadania") // Kolekcja w MongoDB
public class Zadanie {

    @Id
    private String id;  // W MongoDB ID jest Stringiem (ObjectId)

    @NotBlank(message = "Opis zadania nie może być pusty!")
    @Size(min = 5, max = 255, message = "Opis musi zawierać od {min} do {max} znaków!")
    @Field(name = "opis")  // Pole opis w MongoDB
    private String opis;

    @NotBlank(message = "Nazwa zadania nie może być pusta!")
    @Size(min = 3, max = 100, message = "Nazwa zadania musi zawierać od {min} do {max} znaków!")
    @Field(name = "nazwa") // Pole nazwa w MongoDB
    private String nazwa;

    @Field(name = "kolejnosc")
    private Integer kolejnosc; // Kolejność zadania

    @Field(name = "projektId")
    private String projektId; // Powiązanie z projektem (ID projektu)

    @CreatedDate
    @Field(name = "dataczas_utworzenia")
    private LocalDateTime createdDate; // Data utworzenia zadania

    @LastModifiedDate
    @Field(name = "dataczas_modyfikacji")
    private LocalDateTime lastModifiedDate; // Data ostatniej modyfikacji zadania

    // Lista studentów przypisanych do zadania (przechowujemy tylko ID studentów)
    private Set<String> studenciIds;

    // Pusty konstruktor (wymagany przez Spring Data)
    public Zadanie() {}

    // Konstruktor z wymaganymi polami
    public Zadanie(String nazwa, String opis, Integer kolejnosc, String projektId) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.kolejnosc = kolejnosc;
        this.projektId = projektId;
    }

    // Gettery i Settery
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public Integer getKolejnosc() { return kolejnosc; }
    public void setKolejnosc(Integer kolejnosc) { this.kolejnosc = kolejnosc; }

    public String getProjektId() { return projektId; }
    public void setProjektId(String projektId) { this.projektId = projektId; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public Set<String> getStudenciIds() { return studenciIds; }
    public void setStudenciIds(Set<String> studenciIds) { this.studenciIds = studenciIds; }

    // Metoda getZadanieId(), odpowiadająca metodzie getId()
    public String getZadanieId() {
        return this.id;
    }
}

