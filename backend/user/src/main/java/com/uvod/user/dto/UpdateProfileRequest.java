package com.uvod.user.dto;

import com.uvod.user.model.SupportedLocale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user profile.
 * Only safe fields — id, email, createdAt, favorites are NOT modifiable.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    private String avatarUrl;

    private SupportedLocale locale;
}
