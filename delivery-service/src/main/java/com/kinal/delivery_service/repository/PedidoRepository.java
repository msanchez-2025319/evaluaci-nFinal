package com.kinal.delivery_service.repository;

import com.kinal.delivery_service.entity.Pedido;
import com.kinal.delivery_service.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);

    List<Pedido> findByEstadoAndRepartidorIdIsNull(
            EstadoPedido estado
    );

    List<Pedido> findByEstado(EstadoPedido estado);
}