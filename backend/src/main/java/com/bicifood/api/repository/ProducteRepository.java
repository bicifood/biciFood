package com.bicifood.api.repository;

import com.bicifood.api.entity.Producte;
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
 * Repository per l'entitat Producte
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface ProducteRepository extends JpaRepository<Producte, Integer> {

    /**
     * Troba un producte pel seu nom
     * @param nom el nom del producte
     * @return Optional amb el producte si existeix
     */
    Optional<Producte> findByNom(String nom);

    /**
     * Comprova si existeix un producte amb aquest nom
     * @param nom el nom del producte
     * @return true si existeix, false altrament
     */
    boolean existsByNom(String nom);

    /**
     * Troba productes per categoria
     * @param categoriaId l'ID de la categoria
     * @return llista de productes d'aquesta categoria
     */
    List<Producte> findByCategoriaId(Integer categoriaId);

    /**
     * Troba productes per categoria amb paginació
     * @param categoriaId l'ID de la categoria
     * @param pageable paginació
     * @return pàgina de productes d'aquesta categoria
     */
    Page<Producte> findByCategoriaId(Integer categoriaId, Pageable pageable);

    /**
     * Troba productes per nom (cerca parcial, case-insensitive)
     * @param nom part del nom del producte
     * @param pageable paginació
     * @return pàgina de productes que coincideixen
     */
    @Query("SELECT p FROM Producte p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    Page<Producte> findByNomContainingIgnoreCase(@Param("nom") String nom, Pageable pageable);

    /**
     * Troba productes amb stock disponible (stock > 0)
     * @return llista de productes amb stock
     */
    @Query("SELECT p FROM Producte p WHERE p.stock > 0")
    List<Producte> findAvailableProducts();

    /**
     * Troba productes amb stock baix (menys del límit especificat)
     * @param limit el límit de stock
     * @return llista de productes amb stock baix
     */
    @Query("SELECT p FROM Producte p WHERE p.stock <= :limit AND p.stock > 0")
    List<Producte> findLowStockProducts(@Param("limit") Integer limit);

    /**
     * Troba productes sense stock
     * @return llista de productes sense stock
     */
    @Query("SELECT p FROM Producte p WHERE p.stock = 0")
    List<Producte> findOutOfStockProducts();

    /**
     * Troba productes per rang de preus
     * @param minPreu preu mínim
     * @param maxPreu preu màxim
     * @param pageable paginació
     * @return pàgina de productes en el rang de preus
     */
    @Query("SELECT p FROM Producte p WHERE p.preu BETWEEN :minPreu AND :maxPreu")
    Page<Producte> findByPreuBetween(@Param("minPreu") BigDecimal minPreu, 
                                     @Param("maxPreu") BigDecimal maxPreu, 
                                     Pageable pageable);

    /**
     * Troba productes ordenats per preu (ascendent)
     * @param pageable paginació
     * @return pàgina de productes ordenats per preu
     */
    Page<Producte> findAllByOrderByPreuAsc(Pageable pageable);

    /**
     * Troba productes ordenats per preu (descendent)
     * @param pageable paginació
     * @return pàgina de productes ordenats per preu descendent
     */
    Page<Producte> findAllByOrderByPreuDesc(Pageable pageable);

    /**
     * Troba productes més populars (basats en vendes - LiniaComanda)
     * @param limit número màxim de resultats
     * @return llista dels productes més populars
     */
    @Query("SELECT p FROM Producte p " +
           "JOIN p.liniesComanda lc " +
           "GROUP BY p " +
           "ORDER BY SUM(lc.quantitat) DESC")
    List<Producte> findMostPopularProducts(Pageable pageable);

    /**
     * Cerca productes per nom o descripció (cerca global)
     * @param searchTerm terme de cerca
     * @param pageable paginació
     * @return pàgina de productes que coincideixen
     */
    @Query("SELECT p FROM Producte p WHERE " +
           "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.descripcio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Producte> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Troba productes per categoria i amb stock disponible
     * @param categoriaId l'ID de la categoria
     * @return llista de productes disponibles d'aquesta categoria
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.stock > 0")
    List<Producte> findAvailableProductsByCategory(@Param("categoriaId") Integer categoriaId);
}