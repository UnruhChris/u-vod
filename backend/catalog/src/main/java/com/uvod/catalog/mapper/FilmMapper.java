package com.uvod.catalog.mapper;

import com.uvod.catalog.dto.FilmResponse;
import com.uvod.catalog.model.Film;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper to convert between Film entity and FilmResponse DTO.
 *
 * Fields have the same name and type, so the mapping is automatic.
 */
@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmResponse toResponse(Film film);

    List<FilmResponse> toResponseList(List<Film> films);
}
