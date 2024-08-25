package com.jerzyboksa.fishtracker.models.responses;

public record AuthResponse(
    String token,
    String username,
    Long userId,
    Long expirationDate
) {
}
