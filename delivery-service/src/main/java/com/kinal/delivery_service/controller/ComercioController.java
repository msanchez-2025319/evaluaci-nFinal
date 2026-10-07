
package com.kinal.delivery_service.controller;

import com.kinal.delivery_service.dto.*;
import com.kinal.delivery_service.enums.CategoriaComercio;
import com.kinal.delivery_service.service.ComercioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comercios")
@RequiredArgsConstructor
public class ComercioController {

    private final ComercioService comercioService;

    // Consultar comercios abiertos y filtrar por categoría
    @GetMapping
    public ResponseEntity<List<ComercioResponse>> listarComercios(
            @RequestParam(required = false) CategoriaComercio categoria
    ) {
        return ResponseEntity.ok(
                comercioService.listarComercios(categoria)
        );
    }

    // Registrar un comercio
    @PostMapping
    public ResponseEntity<ComercioResponse> crearComercio(
            @Valid @RequestBody ComercioRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comercioService.crearComercio(request));
    }

    // Consultar productos de un comercio
    @GetMapping("/{id}/productos")
    public ResponseEntity<List<ProductoResponse>> listarProductos(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                comercioService.listarProductos(id)
        );
    }

    // Registrar un producto dentro de un comercio
    @PostMapping("/{id}/productos")
    public ResponseEntity<ProductoResponse> crearProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comercioService.crearProducto(id, request));
    }
}
