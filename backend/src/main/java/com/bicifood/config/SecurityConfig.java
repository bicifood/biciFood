package com.bicifood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuració de seguretat per a l'aplicació BiciFood
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean per encriptar contrasenyes
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuració de seguretat bàsica per presentació
     * Protegeix endpoints crítics però manté funcionalitat
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitat per simplicitat en desenvolupament
            .authorizeHttpRequests(auth -> auth
                // Endpoints públics (per frontend i presentació)
                .requestMatchers("/api/health", "/api/test/**").permitAll()
                .requestMatchers("/api/usuaris/create", "/api/usuaris").permitAll() // Crear usuaris
                .requestMatchers("/api/categories/**", "/api/productes/**").permitAll() // Catàleg públic
                .requestMatchers("/h2-console/**").permitAll() // Console H2 per desenvolupament
                // Tots els altres requereixen autenticació
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()) // Per H2 console
            );
        
        return http.build();
    }
}