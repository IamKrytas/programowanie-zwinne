package com.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "zadania")
public class Zadanie {

    @Id
    private String id; // Id dla MongoDB

    private String opis; // Pole opis
    private String nazwa; // Nowe pole nazwa
    private Integer kolejnosc; // Nowe pole kolejnosc
    private String projektId; // Powiązanie z projektem jako String (w MongoDB nie ma relacji jak w relacyjnych DB)

    public Zadanie() {}

    public Zadanie(String nazwa, String opis, Integer kolejnosc, String projektId) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.kolejnosc = kolejnosc;
        this.projektId = projektId;
    }

    // Gettery i Settery

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(Integer kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public String getProjektId() {
        return projektId;
    }

    public void setProjektId(String projektId) {
        this.projektId = projektId;
    }
}
