package com.bicifood.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuració bàsica de Spring Security per BiciFood
 * Configuració temporal per al desenvolupament - permet tots els accessos
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF per APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            
            // Configurar autoritzacions - TEMPORAL: permet tot durant desenvolupament
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            
            // Configurar headers per a H2 Console
            .headers(headers -> headers
                .frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
            )
            
            // Deshabilitar creació de sessions (API REST stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                )
            );

        return http.build();
    }
}

/*
 * NOTA: Aquesta és una configuració temporal per al desenvolupament.
 * 
 * Per a producció, implementar:
 * 1. Autenticació JWT
 * 2. Autorització per rols (ADMIN, CLIENT, REPARTIDOR)
 * 3. Protecció d'endpoints sensibles
 * 4. Rate limiting
 * 5. Validació de tokens
 * 
 * Exemple de configuració per producció:
 * 
 * .authorizeHttpRequests(auth -> auth
 *     .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
 *     .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
 *     .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
 *     .requestMatchers("/admin/**").hasRole("ADMIN")
 *     .requestMatchers("/orders/**").hasAnyRole("CLIENT", "ADMIN", "REPARTIDOR")
 *     .anyRequest().authenticated()
 * )
 */