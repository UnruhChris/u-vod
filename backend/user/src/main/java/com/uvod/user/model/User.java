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

    private String visibleUsername;

    private String email;

    private String identityProvider;

    private String providerUsername;

    private Instant registrationDate;

    private List<String> favorites;

    private List<String> watchHistory;
}
