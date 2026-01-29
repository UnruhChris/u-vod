package com.uvod.user.mapper;

import com.uvod.user.dto.UserResponse;
import com.uvod.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;

/**
 * Mapper per convertire tra entity User e DTO UserResponse.
 * 
 * componentModel = "spring" → MapStruct genera una classe @Component
 * che può essere iniettata con @Autowired.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte User entity in UserResponse DTO.
     * 
     * Mapping automatici (stesso nome):
     * - id → id
     * - visibleUsername → visibleUsername
     * - email → email
     * - favorites → favorites
     * - watchHistory → watchHistory
     * 
     * Mapping custom:
     * - registrationDate (Instant) → registrationDate (String)
     */
    @Mapping(target = "registrationDate", source = "registrationDate", qualifiedByName = "instantToString")
    UserResponse toResponse(User user);

    /**
     * Converte Instant in String ISO-8601.
     * Es: "2026-01-27T14:30:00Z"
     */
    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}