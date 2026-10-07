
package com.kinal.delivery_service.security;

import com.kinal.delivery_service.enums.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey obtenerClave() {
        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long obtenerUsuarioId(Authentication authentication) {
        Claims claims = obtenerClaimsAutenticacion(authentication);

        Number usuarioId = claims.get("usuarioId", Number.class);

        if (usuarioId == null) {
            throw new IllegalArgumentException(
                    "El token no contiene el ID del usuario"
            );
        }

        return usuarioId.longValue();
    }

    public Rol obtenerRol(Authentication authentication) {
        Claims claims = obtenerClaimsAutenticacion(authentication);

        String rol = claims.get("rol", String.class);

        if (rol == null) {
            throw new IllegalArgumentException(
                    "El token no contiene el rol del usuario"
            );
        }

        return Rol.valueOf(rol);
    }

    private Claims obtenerClaimsAutenticacion(
            Authentication authentication
    ) {
        if (authentication == null
                || !(authentication.getCredentials() instanceof String token)) {
            throw new IllegalArgumentException(
                    "No se encontró un token JWT válido"
            );
        }

        return obtenerClaims(token);
    }
}
