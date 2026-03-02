package com.uvod.catalog.service;

import com.uvod.catalog.dto.FilmResponse;
import com.uvod.catalog.exception.FilmNotFoundException;
import com.uvod.catalog.mapper.FilmMapper;
import com.uvod.catalog.model.Film;
import com.uvod.catalog.repository.FilmRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for film catalog management.
 * Read-only operations.
 */
@Service
public class CatalogService {

    private final FilmRepo filmRepo;
    private final FilmMapper filmMapper;

    public CatalogService(FilmRepo filmRepo, FilmMapper filmMapper) {
        this.filmRepo = filmRepo;
        this.filmMapper = filmMapper;
    }

    /**
     * Returns all films in the catalog.
     */
    public List<FilmResponse> getAllFilms() {
        List<Film> films = filmRepo.findAllFilms();
        return filmMapper.toResponseList(films);
    }

    /**
     * Returns a single film by ID.
     *
     * @param id UUID of the film
     * @throws FilmNotFoundException if the film does not exist
     */
    public FilmResponse getFilmById(String id) {
        Film film = filmRepo.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
        return filmMapper.toResponse(film);
    }

    /**
     * Returns a single film by slug.
     *
     * @param slug slug of the film
     * @return FilmResponse
     */
    public FilmResponse getFilmBySlug(String slug) {
        Film film = filmRepo.findBySlug(slug);
        if (film == null)
            throw new FilmNotFoundException(slug);
        return filmMapper.toResponse(film);
    }

    /**
     * Returns films filtered by genre.
     * Leverages the Partition Key for efficient queries.
     *
     * @param genre film genre (e.g. "Sci-Fi")
     */
    public List<FilmResponse> getFilmsByGenre(String genre) {
        List<Film> films = filmRepo.findByGenre(genre);
        return filmMapper.toResponseList(films);
    }

    /**
     * Searches films by title (contains the query).
     * Cross-partition query — acceptable for a small catalog.
     *
     * @param query string to search in the title
     */
    public List<FilmResponse> searchFilms(String query) {
        List<Film> films = filmRepo.searchByTitle(query);
        return filmMapper.toResponseList(films);
    }
}
