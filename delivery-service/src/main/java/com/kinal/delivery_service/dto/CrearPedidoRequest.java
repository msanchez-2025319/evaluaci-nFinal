
package com.kinal.delivery_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CrearPedidoRequest(

        @Positive(message = "El ID del comercio debe ser positivo")
        Long comercioId,

        @NotEmpty(message = "El pedido debe contener al menos un producto")
        @JsonAlias("items")
        List<@NotNull @Valid DetallePedidoRequest> detalles

) {
}
