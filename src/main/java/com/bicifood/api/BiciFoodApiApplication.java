package com.bicifood.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Classe principal de l'aplicació BiciFood API
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaRepositories
public class BiciFoodApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiciFoodApiApplication.class, args);
        System.out.println("🚴‍♂️ BiciFood API is running!");
        System.out.println("📖 Swagger UI: http://localhost:8080/api/v1/swagger-ui.html");
        System.out.println("🏥 Health Check: http://localhost:8080/api/v1/actuator/health");
    }
}