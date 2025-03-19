package com.project.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.project.model.Zadanie;

public interface ZadanieRepository extends MongoRepository<Zadanie, String> {
    @Query("{ 'projekt.projektId' : ?0 }")
    Page<Zadanie> findZadaniaProjektu(String projektId, Pageable pageable);

    @Query("{ 'projekt.projektId' : ?0 }")
    List<Zadanie> findZadaniaProjektu(String projektId);
}
