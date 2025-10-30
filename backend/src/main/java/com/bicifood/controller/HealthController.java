package com.bicifood.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller per verificar l'estat del sistema
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "BiciFood Backend API");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Benvingut a l'API de BiciFood!");
        response.put("endpoints", Map.of(
            "health", "/api/health",
            "usuaris", "/api/usuaris",
            "categories", "/api/categories", 
            "productes", "/api/productes",
            "comandes", "/api/comandes",
            "auth", "/api/auth",
            "admin", "/api/admin"
        ));
        response.put("documentation", "Consulta el README.md per més informació");
        
        return ResponseEntity.ok(response);
    }
}