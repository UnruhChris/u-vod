package com.uvod.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmResponse {
    private String id;
    private String title;
    private String description;
    private int releaseYear;
    private String genre;
    private int durationInMinutes;
    private String thumbnailUrl;
    private String blobName;
    private String slug;
    private List<String> cast;
    private List<String> tags;
}
