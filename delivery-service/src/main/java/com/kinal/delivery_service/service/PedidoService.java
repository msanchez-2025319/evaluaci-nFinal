
package com.kinal.delivery_service.service;

import com.kinal.delivery_service.dto.*;
import com.kinal.delivery_service.entity.*;
import com.kinal.delivery_service.enums.EstadoPedido;
import com.kinal.delivery_service.enums.Rol;
import com.kinal.delivery_service.exception.*;
import com.kinal.delivery_service.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final BigDecimal COSTO_ENVIO =
            new BigDecimal("20.00");

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ComercioRepository comercioRepository;

    // Crear pedido y descontar inventario
    @Transactional
    public PedidoResponse crearPedido(
            Long clienteId,
            CrearPedidoRequest request
    ) {

        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException(
                    "El cliente no es válido"
            );
        }

        Comercio comercio = comercioRepository
                .findById(request.comercioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el comercio solicitado"
                ));

        if (!comercio.isAbierto()) {
            throw new IllegalArgumentException(
                    "El comercio se encuentra cerrado"
            );
        }

        // Agrupar productos y evitar IDs repetidos
        Map<Long, Integer> cantidades = new TreeMap<>();

        for (DetallePedidoRequest detalle : request.detalles()) {

            cantidades.merge(
                    detalle.productoId(),
                    detalle.cantidad(),
                    Math::addExact
            );
        }

        Pedido pedido = Pedido.builder()
                .clienteId(clienteId)
                .fechaPedido(LocalDateTime.now())
                .costoEnvio(COSTO_ENVIO)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        BigDecimal subtotalPedido = BigDecimal.ZERO;

        // Se bloquean los productos en orden para evitar
        // compras simultáneas con inventario incorrecto
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {

            Long productoId = entrada.getKey();
            Integer cantidad = entrada.getValue();

            Producto producto = productoRepository
                    .findByIdForUpdate(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el producto con ID: " + productoId
                    ));

            if (!producto.getComercio().getId()
                    .equals(comercio.getId())) {
                throw new IllegalArgumentException(
                        "El producto no pertenece al comercio"
                );
            }

            if (!producto.isDisponible()) {
                throw new IllegalArgumentException(
                        "El producto no está disponible"
                );
            }

            if (producto.getStock() < cantidad) {
                throw new InsufficientStockException(
                        "Stock insuficiente para: " + producto.getNombre()
                );
            }

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(cantidad));

            producto.setStock(producto.getStock() - cantidad);

            DetallePedido detallePedido = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();

            pedido.getDetalles().add(detallePedido);

            subtotalPedido = subtotalPedido.add(subtotal);
        }

        pedido.setMontoTotal(subtotalPedido.add(COSTO_ENVIO));

        Pedido guardado = pedidoRepository.save(pedido);

        return convertirPedido(guardado);
    }

    // Consultar pedidos del cliente autenticado
    @Transactional(readOnly = true)
    public List<PedidoResponse> misPedidos(Long clienteId) {

        return pedidoRepository
                .findByClienteIdOrderByFechaPedidoDesc(clienteId)
                .stream()
                .map(this::convertirPedido)
                .toList();
    }

    // Consultar pedidos disponibles para repartidores
    @Transactional(readOnly = true)
    public List<PedidoResponse> pedidosDisponibles() {

        return pedidoRepository
                .findByEstadoAndRepartidorIdIsNull(
                        EstadoPedido.PENDIENTE
                )
                .stream()
                .map(this::convertirPedido)
                .toList();
    }

    // Actualizar estado del pedido
    @Transactional
    public PedidoResponse actualizarEstado(
            Long pedidoId,
            Long repartidorId,
            Rol rol,
            ActualizarEstadoRequest request
    ) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el pedido con ID: " + pedidoId
                ));

        EstadoPedido actual = pedido.getEstado();
        EstadoPedido nuevo = request.estado();

        boolean transicionValida = switch (actual) {
            case PENDIENTE ->
                    nuevo == EstadoPedido.EN_PREPARACION;
            case EN_PREPARACION ->
                    nuevo == EstadoPedido.EN_CAMINO;
            case EN_CAMINO ->
                    nuevo == EstadoPedido.ENTREGADO;
            default -> false;
        };

        if (!transicionValida) {
            throw new InvalidStatusException(
                    "No se permite cambiar de " + actual + " a " + nuevo
            );
        }

        if (rol == Rol.REPARTIDOR) {

            if (repartidorId == null || repartidorId <= 0) {
                throw new IllegalArgumentException(
                        "El repartidor no es válido"
                );
            }

            if (pedido.getRepartidorId() != null
                    && !pedido.getRepartidorId().equals(repartidorId)) {
                throw new InvalidStatusException(
                        "El pedido está asignado a otro repartidor"
                );
            }

            if (pedido.getRepartidorId() == null) {
                pedido.setRepartidorId(repartidorId);
            }
        }

        pedido.setEstado(nuevo);

        return convertirPedido(pedidoRepository.save(pedido));
    }

    // Cancelar pedido y devolver el inventario
    @Transactional
    public PedidoResponse cancelarPedido(
            Long pedidoId,
            Long usuarioId,
            Rol rol
    ) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el pedido con ID: " + pedidoId
                ));

        if (rol == Rol.CLIENTE
                && !pedido.getClienteId().equals(usuarioId)) {
            throw new IllegalArgumentException(
                    "No puedes cancelar pedidos de otros clientes"
            );
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new InvalidStatusException(
                    "Solo se pueden cancelar pedidos pendientes"
            );
        }

        List<DetallePedido> detalles = new ArrayList<>(
                pedido.getDetalles()
        );

        detalles.sort(
                Comparator.comparing(d -> d.getProducto().getId())
        );

        for (DetallePedido detalle : detalles) {

            Producto producto = productoRepository
                    .findByIdForUpdate(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe un producto del pedido"
                    ));

            producto.setStock(
                    Math.addExact(
                            producto.getStock(),
                            detalle.getCantidad()
                    )
            );
        }

        pedido.setEstado(EstadoPedido.CANCELADO);

        return convertirPedido(pedidoRepository.save(pedido));
    }

    // Convertir pedido a DTO de respuesta
    private PedidoResponse convertirPedido(Pedido pedido) {

        List<DetallePedidoResponse> detalles = pedido
                .getDetalles()
                .stream()
                .map(detalle -> new DetallePedidoResponse(
                        detalle.getProducto().getId(),
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()
                ))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getRepartidorId(),
                pedido.getFechaPedido(),
                pedido.getCostoEnvio(),
                pedido.getMontoTotal(),
                pedido.getEstado(),
                detalles
        );
    }
}
