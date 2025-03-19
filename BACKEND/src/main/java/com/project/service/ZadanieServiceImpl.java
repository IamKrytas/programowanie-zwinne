package com.project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Zadanie;
import com.project.repository.ZadanieRepository;
import com.project.repository.ProjektRepository;
import com.project.model.Projekt;

@Service
public class ZadanieServiceImpl implements ZadanieService {

    private final ZadanieRepository zadanieRepository;
    private final ProjektRepository projektRepository; // Dodano repozytorium projektów

    @Autowired
    public ZadanieServiceImpl(ZadanieRepository zadanieRepository, ProjektRepository projektRepository) {
        this.zadanieRepository = zadanieRepository;
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<Zadanie> getZadanie(String zadanieId) {
        return zadanieRepository.findById(zadanieId);
    }

    @Override
    public Zadanie setZadanie(Zadanie zadanie) {
        return zadanieRepository.save(zadanie);
    }

    @Override
    @Transactional
    public void deleteZadanie(String zadanieId) {
        Optional<Zadanie> zadanieOpt = zadanieRepository.findById(zadanieId);

        if (zadanieOpt.isPresent()) {
            Zadanie zadanie = zadanieOpt.get();
            String projektId = zadanie.getProjektId();

            // Usuń zadanie z listy zadań w projekcie
            Optional<Projekt> projektOpt = projektRepository.findById(projektId);
            if (projektOpt.isPresent()) {
                Projekt projekt = projektOpt.get();
                projekt.getZadaniaIds().remove(zadanieId);
                projektRepository.save(projekt);
            }

            // Usuń zadanie z bazy
            zadanieRepository.deleteById(zadanieId);
        }
    }

    @Override
    public Page<Zadanie> getZadania(Pageable pageable) {
        return zadanieRepository.findAll(pageable);
    }

    @Override
    public List<Zadanie> findZadaniaProjektu(String projektId) {
        return zadanieRepository.findZadaniaProjektu(projektId);
    }

    @Transactional
    public void deleteAllZadaniaByProjekt(String projektId) {
        List<Zadanie> zadania = zadanieRepository.findZadaniaProjektu(projektId);
        zadanieRepository.deleteAll(zadania);
    }
}
