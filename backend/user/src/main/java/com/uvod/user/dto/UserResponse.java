package com.uvod.user.dto;

import java.util.List;

import com.uvod.user.model.FavoriteItem;
import com.uvod.user.model.SupportedLocale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private SupportedLocale locale;
    private String createdAt;
    private String updatedAt;
    private List<FavoriteItem> favorites;
}
