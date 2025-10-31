package com.bicifood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Classe principal de l'aplicació BiciFood Spring Boot
 * 
 * Aquesta classe configura i inicialitza l'aplicació web BiciFood
 * utilitzant Spring Boot amb configuració automàtica.
 */
@SpringBootApplication
public class BiciFoodApplication extends SpringBootServletInitializer {

    /**
     * Mètode principal per executar l'aplicació Spring Boot
     * 
     * @param args arguments de la línia de comandaments
     */
    public static void main(String[] args) {
        SpringApplication.run(BiciFoodApplication.class, args);
    }
}