package com.bicifood.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🚴‍♂️ BiciFood API Application - Java 21 LTS
 * 
 * API REST per la plataforma de menjar a domicili sostenible BiciFood.
 * 
 * 🚀 Tecnologies modernes:
 * - Java 21 LTS amb Virtual Threads (Project Loom)
 * - Spring Boot 3.3.5
 * - Records per DTOs immutables
 * - Pattern Matching avançat
 * 
 * 📋 Funcionalitats:
 * - Categories i productes
 * - Usuaris i autenticació JWT
 * - Comandes i cistella
 * - Lliuraments amb bicicletes
 * 
 * @author BiciFood Team
 * @version 2.0.0-Java21
 */
@SpringBootApplication
public class BicifoodApiApplication {

    public static void main(String[] args) {
        // Informació runtime Java 21
        var javaVersion = System.getProperty("java.version");
        boolean virtualThreadsEnabled = isVirtualThreadsEnabled();
        
        SpringApplication.run(BicifoodApiApplication.class, args);
        
        System.out.println("🚴‍♂️ BiciFood API started successfully!");
        System.out.println("☕ Java Version: " + javaVersion);
        System.out.println("⚡ Virtual Threads: " + (virtualThreadsEnabled ? "✅ Enabled" : "❌ Disabled"));
        System.out.println("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
    }
    
    /**
     * Comprova si Virtual Threads estan disponibles
     */
    private static boolean isVirtualThreadsEnabled() {
        try {
            // Prova crear un Virtual Thread per verificar suport
            var virtualThread = Thread.ofVirtual().name("test").unstarted(() -> {});
            return virtualThread.isVirtual();
        } catch (Exception e) {
            return false;
        }
    }
}