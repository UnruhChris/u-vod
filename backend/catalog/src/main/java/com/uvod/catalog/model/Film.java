package com.uvod.catalog.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Container(containerName = "films")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    @Id
    private String id;

    private String title;

    private String description;

    private int releaseYear;

    @PartitionKey
    private String genre;

    private int durationInMinutes;

    private String thumbnailUrl;

    private String blobName;

    private String slug;

    private List<String> cast;

    private List<String> tags;
}
