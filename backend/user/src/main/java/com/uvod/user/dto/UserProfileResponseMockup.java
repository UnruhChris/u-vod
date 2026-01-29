package com.uvod.user.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseMockup {
    private String userId;
    private String username;
    private List<String> favorites;

}