package com.kinal.evaluacionfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String mensaje;
    private String email;
    private String rol;
}