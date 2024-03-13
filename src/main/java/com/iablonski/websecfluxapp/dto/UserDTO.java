package com.iablonski.websecfluxapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.iablonski.websecfluxapp.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDTO(UUID id,
                      String username,
                      @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                      String password,
                      UserRole role,
                      String firstName,
                      String lastName,
                      boolean activated,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
}
