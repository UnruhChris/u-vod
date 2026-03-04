package com.uvod.user.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for adding a film to favorites.
 * Contains all denormalized display fields from the Catalog.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteRequest {

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    @NotBlank(message = "Slug is required")
    private String slug;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private int releaseYear;

    private String genre;

    private int durationInMinutes;

    @NotBlank(message = "Thumbnail URL is required")
    private String thumbnailUrl;

    private List<String> cast;

    private List<String> tags;
}
