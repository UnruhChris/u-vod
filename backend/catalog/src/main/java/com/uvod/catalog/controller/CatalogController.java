package com.uvod.catalog.controller;

import com.uvod.catalog.dto.FilmResponse;
import com.uvod.catalog.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for the film catalog.
 */
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * GET /catalog
     * Returns all films in the catalog.
     */
    @GetMapping
    public ResponseEntity<List<FilmResponse>> getAllFilms() {
        return ResponseEntity.ok(catalogService.getAllFilms());
    }

    /**
     * GET /catalog/{id}
     * Returns a single film by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilmResponse> getFilmById(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.getFilmById(id));
    }

    /**
     * GET /catalog/genre/{genre}
     * Returns films filtered by genre.
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<FilmResponse>> getFilmsByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(catalogService.getFilmsByGenre(genre));
    }

    /**
     * GET /catalog/search?q=...
     * Searches films by title.
     */
    @GetMapping("/search")
    public ResponseEntity<List<FilmResponse>> searchFilms(@RequestParam("q") String query) {
        return ResponseEntity.ok(catalogService.searchFilms(query));
    }

    /**
     * GET /catalog/slug/{slug}
     * Returns a single film by slug.
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<FilmResponse> getFilmBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(catalogService.getFilmBySlug(slug));
    }
}
