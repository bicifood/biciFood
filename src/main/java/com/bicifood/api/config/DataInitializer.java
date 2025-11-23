package com.bicifood.api.config;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inicializador de datos para BiciFood
 * Ejecuta data.sql después de que Hibernate cree las tablas
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    private final DataSource dataSource;

    public DataInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Pequeña pausa para asegurar que Hibernate terminó de crear tablas
            Thread.sleep(2000);

            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("data.sql"));
            populator.execute(dataSource);
            logger.info("✅ Datos cargados desde data.sql correctamente");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("❌ Error cargando datos (InterruptedException): " + e.getMessage());
        } catch (Exception e) {
            logger.severe("❌ Error cargando datos: " + e.getMessage());
        }
    }
}
