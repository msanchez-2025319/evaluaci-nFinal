package com.kinal.delivery_service.entity;

import com.kinal.delivery_service.enums.CategoriaComercio;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comercios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaComercio categoria;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private boolean abierto;

    @OneToMany(mappedBy = "comercio")
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}