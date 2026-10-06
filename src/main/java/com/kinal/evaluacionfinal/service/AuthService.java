package com.kinal.evaluacionfinal.service;

import com.kinal.evaluacionfinal.dto.AuthResponse;
import com.kinal.evaluacionfinal.dto.RegisterRequest;
import com.kinal.evaluacionfinal.entity.Usuario;
import com.kinal.evaluacionfinal.enums.Rol;
import com.kinal.evaluacionfinal.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.CLIENTE)
                .build();

        usuarioRepository.save(usuario);

        return new AuthResponse(
                "Usuario registrado correctamente",
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }
}