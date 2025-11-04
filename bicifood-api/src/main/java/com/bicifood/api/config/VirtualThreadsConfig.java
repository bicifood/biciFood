package com.bicifood.api.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

/**
 * ⚡ Configuració Java 21 Virtual Threads per BiciFood API
 * Millora significativament el rendiment per aplicacions I/O intensives
 */
@Configuration
public class VirtualThreadsConfig {

    /**
     * Configura Virtual Threads per el servidor web (Tomcat)
     * Els Virtual Threads permeten manejar milers de connexions simultànies
     * amb molt poc overhead de memòria i CPU
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    /**
     * Executor per tasques asíncrones usant Virtual Threads
     * Perfecte per operacions de base de dades i calls HTTP
     */
    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}