package com.kinal.delivery_service.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(

        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal

) {
}