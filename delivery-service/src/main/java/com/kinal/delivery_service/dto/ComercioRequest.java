package com.kinal.delivery_service.dto;

import com.kinal.delivery_service.enums.CategoriaComercio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComercioRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "La categoría es obligatoria")
        CategoriaComercio categoria,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @NotNull(message = "Debe indicar si el comercio está abierto")
        Boolean abierto

) {
}