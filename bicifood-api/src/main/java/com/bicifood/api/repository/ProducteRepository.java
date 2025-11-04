package com.bicifood.api.repository;

import com.bicifood.api.model.Producte;
import com.bicifood.api.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 🗄️ Repository per Producte
 * Proporciona operacions CRUD i consultes personalitzades per productes
 */
@Repository
public interface ProducteRepository extends JpaRepository<Producte, Integer> {

    /**
     * Busca productes per categoria
     * @param categoria La categoria dels productes
     * @return Llista de productes de la categoria
     */
    List<Producte> findByCategoria(Categoria categoria);

    /**
     * Busca productes per ID de categoria
     * @param idCategoria ID de la categoria
     * @return Llista de productes de la categoria
     */
    List<Producte> findByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Busca productes pel nom (contenint text)
     * @param nom Text a buscar en el nom
     * @return Llista de productes que contenen el text
     */
    @Query("SELECT p FROM Producte p WHERE p.nom LIKE %:nom%")
    List<Producte> findByNomContaining(@Param("nom") String nom);

    /**
     * Busca productes disponibles (amb stock > 0)
     * @return Llista de productes amb stock
     */
    @Query("SELECT p FROM Producte p WHERE p.stock > 0")
    List<Producte> findAvailableProducts();

    /**
     * Busca productes per rang de preu
     * @param preuMin Preu mínim
     * @param preuMax Preu màxim
     * @return Llista de productes dins del rang
     */
    @Query("SELECT p FROM Producte p WHERE p.preu BETWEEN :preuMin AND :preuMax")
    List<Producte> findByPreuBetween(@Param("preuMin") BigDecimal preuMin, 
                                     @Param("preuMax") BigDecimal preuMax);

    /**
     * Busca productes per categoria amb paginació
     * @param idCategoria ID de la categoria
     * @param pageable Configuració de paginació
     * @return Pàgina de productes
     */
    Page<Producte> findByCategoriaIdCategoria(Integer idCategoria, Pageable pageable);

    /**
     * Comprova si existeix un producte amb aquest nom
     * @param nom Nom del producte
     * @return true si existeix, false altrament
     */
    boolean existsByNom(String nom);

    /**
     * Obté productes ordenats per preu ascendent
     * @return Llista de productes ordenats per preu
     */
    List<Producte> findAllByOrderByPreuAsc();

    /**
     * Busca productes populars (aquesta consulta es pot personalitzar més tard)
     * @return Llista de productes populars
     */
    @Query("SELECT p FROM Producte p WHERE p.stock > 0 ORDER BY p.nom ASC")
    List<Producte> findPopularProducts();
}