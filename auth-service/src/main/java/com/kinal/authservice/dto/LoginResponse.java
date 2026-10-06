package com.kinal.authservice.dto;

public record LoginResponse(
        String token,
        String tipo,
        String email,
        String rol
) {
}