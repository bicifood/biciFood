package com.bicifood.repository;

import com.bicifood.model.Producte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per l'entitat Producte
 * Utilitza Spring Data JPA per operacions de base de dades
 */
@Repository
public interface ProducteRepository extends JpaRepository<Producte, Long> {

    /**
     * Trobar productes per categoria
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.actiu = true ORDER BY p.nom")
    List<Producte> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Trobar productes per categoria amb stock disponible
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.actiu = true AND p.stock > 0 ORDER BY p.nom")
    List<Producte> findByCategoriaIdAndStockAvailable(@Param("categoriaId") Long categoriaId);

    /**
     * Trobar productes que contenen un text al nom (insensible a majúscules)
     */
    List<Producte> findByNomContainingIgnoreCaseAndActiuTrue(String nom);

    /**
     * Trobar productes amb stock superior a un valor
     */
    List<Producte> findByStockGreaterThanAndActiuTrueOrderByNom(Integer stock);

    /**
     * Trobar productes destacats
     */
    List<Producte> findByDestacatTrueAndActiuTrueOrderByNom();

    /**
     * Trobar productes actius ordenats per nom
     */
    List<Producte> findByActiuTrueOrderByNom();

    /**
     * Trobar productes per categoria i destacats
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.destacat = true AND p.actiu = true ORDER BY p.nom")
    List<Producte> findByCategoriaIdAndDestacatTrue(@Param("categoriaId") Long categoriaId);

    /**
     * Cercar productes per nom o descripció
     */
    @Query("SELECT p FROM Producte p WHERE (LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.descripcio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND p.actiu = true ORDER BY p.nom")
    List<Producte> findByNomOrDescripcioContaining(@Param("searchTerm") String searchTerm);

    /**
     * Trobar productes amb stock baix (menys de 10 unitats)
     */
    @Query("SELECT p FROM Producte p WHERE p.stock < 10 AND p.actiu = true ORDER BY p.stock ASC")
    List<Producte> findLowStockProducts();

    /**
     * Trobar productes amb stock inferior a un valor
     */
    @Query("SELECT p FROM Producte p WHERE p.stock < :stock ORDER BY p.stock ASC")
    List<Producte> findByStockLessThan(@Param("stock") int stock);

    /**
     * Comptar productes actius
     */
    Long countByActiuTrue();

    /**
     * Trobar productes per estat actiu amb paginació
     */
    Page<Producte> findByActiu(Boolean actiu, Pageable pageable);

    /**
     * Trobar productes per categoria i estat actiu amb paginació
     */
    Page<Producte> findByCategoriaIdAndActiu(Long categoriaId, Boolean actiu, Pageable pageable);

    /**
     * Trobar productes amb stock inferior amb paginació
     */
    Page<Producte> findByStockLessThan(int stock, Pageable pageable);

    /**
     * Trobar productes per categoria amb paginació
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId ORDER BY p.nom")
    Page<Producte> findByCategoriaId(@Param("categoriaId") Long categoriaId, Pageable pageable);

    /**
     * Comptar productes per categoria
     */
    @Query("SELECT COUNT(p) FROM Producte p WHERE p.categoria.id = :categoriaId AND p.actiu = true")
    Long countByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Trobar productes més venuts (utilitzant Named Query)
     */
    @Query(name = "Producte.findAvailable")
    List<Producte> findProductesDisponibles();

    /**
     * Actualitzar stock de múltiples productes
     */
    @Query("UPDATE Producte p SET p.stock = p.stock - :quantitat WHERE p.id = :producteId AND p.stock >= :quantitat")
    int reduirStock(@Param("producteId") Long producteId, @Param("quantitat") Integer quantitat);
}