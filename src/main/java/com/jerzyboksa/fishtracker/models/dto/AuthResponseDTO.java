package com.jerzyboksa.fishtracker.models.dto;

public record AuthResponseDTO(
    String token,
    String name,
    Long userId,
    Long expirationDate
) {
}
