package com.kinal.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @NotBlank
        String nombre,

        @NotBlank
        String direccion,

        @NotBlank
        String telefono,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}