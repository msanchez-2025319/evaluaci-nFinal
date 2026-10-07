package com.kinal.delivery_service.dto;

import java.math.BigDecimal;

public record ProductoResponse(

        Long id,
        Long comercioId,
        String nombre,
        BigDecimal precio,
        Integer stock,
        boolean disponible

) {
}