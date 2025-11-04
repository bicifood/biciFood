package com.bicifood.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * 🔒 Configuració de Seguretat per BiciFood API
 * Configuració temporal que permet accés lliure per desenvolupament
 * TODO: Implementar autenticació JWT per producció
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF per APIs REST
            .csrf(csrf -> csrf.disable())
            
            // CORS s'activa automàticament
            
            // Configurar autoritzacions
            .authorizeHttpRequests(authz -> authz
                // Endpoints públics
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                
                // Per ara, permetre tot per desenvolupament
                .anyRequest().permitAll()
            );

        return http.build();
    }


}