package com.kinal.delivery_service.dto;

import com.kinal.delivery_service.enums.CategoriaComercio;

public record ComercioResponse(

        Long id,
        String nombre,
        CategoriaComercio categoria,
        String direccion,
        boolean abierto

) {
}