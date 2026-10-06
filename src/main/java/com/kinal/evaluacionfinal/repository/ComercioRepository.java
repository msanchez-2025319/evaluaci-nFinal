package com.kinal.evaluacionfinal.repository;

import com.kinal.evaluacionfinal.entity.Comercio;
import com.kinal.evaluacionfinal.enums.CategoriaComercio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    List<Comercio> findByAbiertoTrue();

    List<Comercio> findByAbiertoTrueAndCategoria(CategoriaComercio categoria);
}