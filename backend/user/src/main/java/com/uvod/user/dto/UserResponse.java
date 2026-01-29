package com.uvod.user.dto;

import java.util.List;

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
    private String visibleUsername;
    private String email;
    private String registrationDate;
    private List<String> favorites;
    private List<String> watchHistory;
}
