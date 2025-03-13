package com.project.model;

import jakarta.persistence.Index;

import jakarta.persistence.ManyToMany;

@Entity //Indeksujemy kolumny, które są najczęściej wykorzystywane do wyszukiwania studentów
@Table(name = "student",
		indexes = { @Index(name = "idx_nazwisko", columnList = "nazwisko", unique = false),
					@Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true) })
public class Student {
		/* TODO Uzupełnij kod o zmienne reprezentujące pola tabeli student (patrz rys. 3.1),
		. następnie wygeneruj dla nich akcesory i mutatory (Source -> Generate Getters and Setters)
		 */
public Student() {}

public Student(String imie, String nazwisko, String nrIndeksu, Boolean stacjonarny) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.nrIndeksu = nrIndeksu;
	}
public Student(String imie, String nazwisko, String nrIndeksu, String email, Boolean stacjonarny) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.nrIndeksu = nrIndeksu;
		this.email = email;
		this.stacjonarny = stacjonarny;
	}
//...
}

@ManyToMany(mappedBy = "studenci")
private Set<Projekt> projekty;