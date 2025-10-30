package com.bicifood.service;

import com.bicifood.model.Producte;
import java.util.List;

/**
 * Interfície del servei per gestionar productes
 * Defineix les operacions de negoci per a productes
 */
public interface ProducteService {
    
    /**
     * Obtenir tots els productes
     */
    List<Producte> findAll();
    
    /**
     * Obtenir productes per categoria
     */
    List<Producte> findByCategoria(Long categoriaId);
    
    /**
     * Obtenir un producte per ID
     */
    Producte findById(Long id);
    
    /**
     * Guardar o actualitzar un producte
     */
    Producte save(Producte producte);
    
    /**
     * Eliminar un producte per ID
     */
    void delete(Long id);
    
    /**
     * Actualitzar stock d'un producte
     */
    Producte updateStock(Long id, Integer nouStock);
    
    /**
     * Cercar productes per nom
     */
    List<Producte> searchByNom(String nom);
    
    /**
     * Obtenir productes disponibles (amb stock > 0)
     */
    List<Producte> findAvailable();
    
    /**
     * Obtenir productes destacats
     */
    List<Producte> findDestacats();
}