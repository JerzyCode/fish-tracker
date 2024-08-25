package com.jerzyboksa.fishtracker.models.dto;

public record AuthResponseDTO(
    String token,
    String email,
    Long userId,
    Long expirationDate
) {
}
