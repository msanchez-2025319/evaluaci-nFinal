package com.kinal.evaluacionfinal.repository;

import com.kinal.evaluacionfinal.entity.Pedido;
import com.kinal.evaluacionfinal.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);

    List<Pedido> findByRepartidorIsNullAndEstado(EstadoPedido estado);

    List<Pedido> findByEstado(EstadoPedido estado);
}