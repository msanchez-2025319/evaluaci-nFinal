package com.kinal.authservice.dto;

public record AuthResponse(
        String mensaje,
        String email,
        String rol
) {
}