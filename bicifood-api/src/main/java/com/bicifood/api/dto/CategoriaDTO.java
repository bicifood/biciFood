package com.bicifood.api.dto;

import java.math.BigDecimal;

/**
 * 🏷️ DTO per Categories usant Java Records (Java 21)
 * Records proporcionen equals(), hashCode(), toString() automàticament
 */
public record CategoriaDTO(
    Integer idCategoria,
    String nomCat
) {
    
    /**
     * Constructor compact amb validació
     */
    public CategoriaDTO {
        if (nomCat == null || nomCat.trim().isEmpty()) {
            throw new IllegalArgumentException("El nom de la categoria no pot estar buit");
        }
        // Normalitzar a majúscules
        nomCat = nomCat.trim().toUpperCase();
    }
    
    /**
     * Factory method per crear des d'entitat
     */
    public static CategoriaDTO of(Integer id, String nom) {
        return new CategoriaDTO(id, nom);
    }
}

/**
 * 🍽️ DTO per Productes usant Java Records (Java 21)  
 * Immutable i amb validació automàtica
 */
record ProducteDTO(
    Integer idProducte,
    String nom,
    BigDecimal preu,
    String imatgePath,
    Integer stock,
    String descripcio,
    CategoriaDTO categoria
) {
    
    /**
     * Constructor compact amb validacions
     */
    public ProducteDTO {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("El nom del producte és obligatori");
        }
        if (preu == null || preu.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El preu ha de ser positiu");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("L'stock no pot ser negatiu");
        }
        if (descripcio == null || descripcio.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripció és obligatòria");
        }
    }
    
    /**
     * Método per verificar si està disponible
     */
    public boolean isDisponible() {
        return stock > 0;
    }
    
    /**
     * Método per obtenir preu amb format
     */
    public String getPreuFormatat() {
        return String.format("%.2f €", preu);
    }
}