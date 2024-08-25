package com.jerzyboksa.fishtracker.models.dto;

public record RegisterRequestDTO(
    String email,
    String name,
    String password
) {
}
