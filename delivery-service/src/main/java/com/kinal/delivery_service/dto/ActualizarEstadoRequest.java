package com.kinal.delivery_service.dto;

import com.kinal.delivery_service.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoPedido estado

) {
}