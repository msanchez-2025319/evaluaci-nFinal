
package com.kinal.delivery_service.service;

import com.kinal.delivery_service.dto.*;
import com.kinal.delivery_service.entity.Comercio;
import com.kinal.delivery_service.entity.Producto;
import com.kinal.delivery_service.enums.CategoriaComercio;
import com.kinal.delivery_service.exception.ResourceNotFoundException;
import com.kinal.delivery_service.repository.ComercioRepository;
import com.kinal.delivery_service.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComercioService {

    private final ComercioRepository comercioRepository;
    private final ProductoRepository productoRepository;

    // Registrar un nuevo comercio
    @Transactional
    public ComercioResponse crearComercio(ComercioRequest request) {

        Comercio comercio = Comercio.builder()
                .nombre(request.nombre())
                .categoria(request.categoria())
                .direccion(request.direccion())
                .abierto(request.abierto())
                .build();

        Comercio guardado = comercioRepository.save(comercio);

        return convertirComercio(guardado);
    }

    // Consultar comercios abiertos, con filtro opcional
    @Transactional(readOnly = true)
    public List<ComercioResponse> listarComercios(
            CategoriaComercio categoria
    ) {

        List<Comercio> comercios;

        if (categoria == null) {
            comercios = comercioRepository.findByAbiertoTrue();
        } else {
            comercios = comercioRepository
                    .findByAbiertoTrueAndCategoria(categoria);
        }

        return comercios.stream()
                .map(this::convertirComercio)
                .toList();
    }

    // Agregar un producto a un comercio
    @Transactional
    public ProductoResponse crearProducto(
            Long comercioId,
            ProductoRequest request
    ) {

        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el comercio con ID: " + comercioId
                ));

        Producto producto = Producto.builder()
                .comercio(comercio)
                .nombre(request.nombre())
                .precio(request.precio())
                .stock(request.stock())
                .disponible(request.disponible())
                .build();

        Producto guardado = productoRepository.save(producto);

        return convertirProducto(guardado);
    }

    // Consultar productos de un comercio
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductos(Long comercioId) {

        if (!comercioRepository.existsById(comercioId)) {
            throw new ResourceNotFoundException(
                    "No existe el comercio con ID: " + comercioId
            );
        }

        return productoRepository.findByComercioId(comercioId)
                .stream()
                .map(this::convertirProducto)
                .toList();
    }

    // Convertir entidad Comercio a DTO
    private ComercioResponse convertirComercio(Comercio comercio) {

        return new ComercioResponse(
                comercio.getId(),
                comercio.getNombre(),
                comercio.getCategoria(),
                comercio.getDireccion(),
                comercio.isAbierto()
        );
    }

    // Convertir entidad Producto a DTO
    private ProductoResponse convertirProducto(Producto producto) {

        return new ProductoResponse(
                producto.getId(),
                producto.getComercio().getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.isDisponible()
        );
    }
}
