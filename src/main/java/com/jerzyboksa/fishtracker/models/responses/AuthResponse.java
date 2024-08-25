package com.jerzyboksa.fishtracker.models.responses;

public record AuthResponse(
    String token,
    String username,
    Long id,
    Long expirationDate
) {
}
