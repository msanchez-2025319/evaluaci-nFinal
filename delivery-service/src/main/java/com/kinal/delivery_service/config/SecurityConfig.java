
package com.kinal.delivery_service.config;

import com.kinal.delivery_service.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpStatus.UNAUTHORIZED.value(),
                                                "Debes iniciar sesión"
                                        )
                        )
                        .accessDeniedHandler(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpStatus.FORBIDDEN.value(),
                                                "No tienes permisos"
                                        )
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Permitir procesamiento interno de errores
                        .dispatcherTypeMatchers(
                                DispatcherType.ERROR
                        ).permitAll()

                        // Solo ADMIN puede registrar comercios
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/comercios"
                        ).hasRole("ADMIN")

                        // Solo ADMIN puede registrar productos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/comercios/*/productos"
                        ).hasRole("ADMIN")

                        // Usuarios autenticados consultan comercios
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/comercios",
                                "/api/v1/comercios/*/productos"
                        ).authenticated()

                        // Solo CLIENTE puede crear pedidos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/pedidos"
                        ).hasRole("CLIENTE")

                        // Solo CLIENTE consulta sus pedidos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/pedidos/mis-pedidos"
                        ).hasRole("CLIENTE")

                        // REPARTIDOR y ADMIN consultan pedidos disponibles
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/pedidos/disponibles"
                        ).hasAnyRole("REPARTIDOR", "ADMIN")

                        // REPARTIDOR y ADMIN actualizan estados
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/pedidos/*/estado"
                        ).hasAnyRole("REPARTIDOR", "ADMIN")

                        // CLIENTE y ADMIN cancelan pedidos
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/pedidos/*/cancelar"
                        ).hasAnyRole("CLIENTE", "ADMIN")

                        // Denegar cualquier otra solicitud
                        .anyRequest().denyAll()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
