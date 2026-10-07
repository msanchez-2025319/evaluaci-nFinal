package com.kinal.delivery_service.dto;

import com.kinal.delivery_service.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(

        Long id,
        Long clienteId,
        Long repartidorId,
        LocalDateTime fechaPedido,
        BigDecimal costoEnvio,
        BigDecimal montoTotal,
        EstadoPedido estado,
        List<DetallePedidoResponse> detalles

) {
}