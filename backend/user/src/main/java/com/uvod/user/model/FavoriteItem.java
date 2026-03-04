package com.uvod.user.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denormalized film data embedded in the User document.
 * Contains all display fields needed to render cards and detail views
 * without calling the Catalog service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItem {

    private String movieId;
    private String slug;
    private String title;
    private String description;
    private int releaseYear;
    private String genre;
    private int durationInMinutes;
    private String thumbnailUrl;
    private List<String> cast;
    private List<String> tags;
}
