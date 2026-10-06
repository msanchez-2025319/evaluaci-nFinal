package com.kinal.evaluacionfinal.repository;

import com.kinal.evaluacionfinal.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Long> {
}