package com.project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.model.Zadanie;

public interface ZadanieService {
    Optional<Zadanie> getZadanie(String zadanieId);
    Zadanie setZadanie(Zadanie zadanie);
    void deleteZadanie(String zadanieId);
    Page<Zadanie> getZadania(Pageable pageable);
    List<Zadanie> findZadaniaProjektu(String projektId);
}
