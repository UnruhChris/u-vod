package com.uvod.catalog.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.uvod.catalog.model.Film;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface FilmRepo extends CosmosRepository<Film, String> {

    @Query("SELECT * FROM c")
    List<Film> findAllFilms();

    List<Film> findByGenre(String genre);

    @Query("SELECT * FROM c WHERE CONTAINS(c.title, @query, true)")
    List<Film> searchByTitle(@Param("query") String query);

    Film findBySlug(String slug);
}
