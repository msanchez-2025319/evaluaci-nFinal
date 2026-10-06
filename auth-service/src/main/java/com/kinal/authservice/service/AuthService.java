package com.kinal.authservice.service;

import com.kinal.authservice.dto.AuthResponse;
import com.kinal.authservice.dto.LoginRequest;
import com.kinal.authservice.dto.LoginResponse;
import com.kinal.authservice.dto.RegisterRequest;
import com.kinal.authservice.entity.Usuario;
import com.kinal.authservice.enums.Rol;
import com.kinal.authservice.exception.InvalidCredentialsException;
import com.kinal.authservice.repository.UsuarioRepository;
import com.kinal.authservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre().trim())
                .direccion(request.direccion().trim())
                .telefono(request.telefono().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .rol(Rol.CLIENTE)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        return new AuthResponse(
                "Usuario registrado correctamente",
                guardado.getEmail(),
                guardado.getRol().name()
        );
    }

    public LoginResponse login(LoginRequest request) {

        String email = request.email().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Credenciales incorrectas")
                );

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }

        String token = jwtService.generarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }
}