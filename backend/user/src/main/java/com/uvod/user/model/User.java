package com.uvod.user.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Container(containerName = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @PartitionKey
    private String id;

    private String username;

    private String email;

    private String identityProvider;

    private String avatarUrl;

    private SupportedLocale locale;

    private Instant createdAt;

    private Instant updatedAt;

    private List<FavoriteItem> favorites;
}
