package com.bicifood.api.repository;

import com.bicifood.api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per l'entitat Categoria
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Troba una categoria pel seu nom
     * @param nom el nom de la categoria
     * @return Optional amb la categoria si existeix
     */
    Optional<Categoria> findByNom(String nom);

    /**
     * Comprova si existeix una categoria amb aquest nom
     * @param nom el nom de la categoria
     * @return true si existeix, false altrament
     */
    boolean existsByNom(String nom);

    /**
     * Troba categories per nom (cerca parcial, case-insensitive)
     * @param nom part del nom de la categoria
     * @return llista de categories que coincideixen
     */
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Categoria> findByNomContainingIgnoreCase(String nom);

    /**
     * Troba totes les categories ordenades per nom
     * @return llista de categories ordenades per nom
     */
    @Query("SELECT c FROM Categoria c ORDER BY c.nom")
    List<Categoria> findAllOrderByNom();

    /**
     * Troba categories que tenen productes associats
     * @return llista de categories amb productes
     */
    @Query("SELECT DISTINCT c FROM Categoria c INNER JOIN c.productes p")
    List<Categoria> findCategoriesWithProducts();

    /**
     * Conta el número de productes per categoria
     * @param categoriaId l'ID de la categoria
     * @return número de productes en aquesta categoria
     */
    @Query("SELECT COUNT(p) FROM Producte p WHERE p.categoria.id = :categoriaId")
    Long countProductsByCategory(Integer categoriaId);
}