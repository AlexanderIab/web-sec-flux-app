package com.iablonski.websecfluxapp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import java.util.Date;
import java.util.UUID;


@Builder(toBuilder = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AuthResponseDTO (UUID userId, String token, Date issuedAt, Date expiresAt) {


}
