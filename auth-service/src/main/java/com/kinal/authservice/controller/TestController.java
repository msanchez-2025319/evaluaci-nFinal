package com.kinal.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("JWT válido - acceso permitido");
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> cliente() {
        return ResponseEntity.ok("Acceso permitido para CLIENTE");
    }
}