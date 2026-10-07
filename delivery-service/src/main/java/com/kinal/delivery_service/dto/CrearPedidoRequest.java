package com.kinal.delivery_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CrearPedidoRequest(

        @NotNull(message = "El comercio es obligatorio")
        Long comercioId,

        @NotEmpty(message = "El pedido debe contener al menos un producto")
        List<@NotNull @Valid DetallePedidoRequest> detalles

) {
}