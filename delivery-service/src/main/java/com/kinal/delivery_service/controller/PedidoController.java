
package com.kinal.delivery_service.controller;

import com.kinal.delivery_service.dto.*;
import com.kinal.delivery_service.enums.Rol;
import com.kinal.delivery_service.security.JwtService;
import com.kinal.delivery_service.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final JwtService jwtService;

    // Crear un pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request,
            Authentication authentication
    ) {
        Long clienteId = jwtService.obtenerUsuarioId(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crearPedido(clienteId, request));
    }

    // Consultar pedidos del cliente autenticado
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(
            Authentication authentication
    ) {
        Long clienteId = jwtService.obtenerUsuarioId(authentication);

        return ResponseEntity.ok(
                pedidoService.misPedidos(clienteId)
        );
    }

    // Consultar pedidos disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<PedidoResponse>> pedidosDisponibles() {
        return ResponseEntity.ok(
                pedidoService.pedidosDisponibles()
        );
    }

    // Actualizar el estado de un pedido
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoRequest request,
            Authentication authentication
    ) {
        Long usuarioId = jwtService.obtenerUsuarioId(authentication);
        Rol rol = jwtService.obtenerRol(authentication);

        return ResponseEntity.ok(
                pedidoService.actualizarEstado(
                        id, usuarioId, rol, request
                )
        );
    }

    // Cancelar un pedido
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long usuarioId = jwtService.obtenerUsuarioId(authentication);
        Rol rol = jwtService.obtenerRol(authentication);

        return ResponseEntity.ok(
                pedidoService.cancelarPedido(id, usuarioId, rol)
        );
    }
}
