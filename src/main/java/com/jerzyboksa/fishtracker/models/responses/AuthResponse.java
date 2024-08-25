package com.jerzyboksa.fishtracker.models.responses;

public record AuthResponse(
    String token,
    String email,
    Long userId,
    Long expirationDate
) {
}
