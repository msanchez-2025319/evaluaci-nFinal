package com.kinal.delivery_service.repository;

import com.kinal.delivery_service.entity.Comercio;
import com.kinal.delivery_service.enums.CategoriaComercio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    List<Comercio> findByAbiertoTrue();

    List<Comercio> findByAbiertoTrueAndCategoria(
            CategoriaComercio categoria
    );
}