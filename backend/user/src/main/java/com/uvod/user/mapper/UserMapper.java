package com.uvod.user.mapper;

import com.uvod.user.dto.UserResponse;
import com.uvod.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;

/**
 * Mapper to convert between User entity and UserResponse DTO.
 * 
 * componentModel = "spring" → MapStruct generates a @Component class
 * that can be injected with @Autowired.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts User entity to UserResponse DTO.
     * 
     * Automatic mappings (same name):
     * - id → id
     * - visibleUsername → visibleUsername
     * - email → email
     * - favorites → favorites
     * - watchHistory → watchHistory
     * 
     * Custom mapping:
     * - registrationDate (Instant) → registrationDate (String)
     */
    @Mapping(target = "registrationDate", source = "registrationDate", qualifiedByName = "instantToString")
    UserResponse toResponse(User user);

    /**
     * Converts Instant to ISO-8601 String.
     * E.g.: "2026-01-27T14:30:00Z"
     */
    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}