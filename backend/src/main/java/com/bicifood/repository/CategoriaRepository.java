package com.bicifood.repository;

import com.bicifood.model.Categoria;
import com.bicifood.model.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per gestionar categories a la base de dades
 * Utilitza Spring Data JPA per operacions CRUD i consultes personalitzades
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Trobar categories per nom exacte
     */
    List<Categoria> findByNom(String nom);

    /**
     * Trobar categories per nom excepte un ID específic (per validar duplicats en actualitzacions)
     */
    @Query("SELECT c FROM Categoria c WHERE c.nom = :nom AND c.id != :id")
    List<Categoria> findByNomAndIdNot(@Param("nom") String nom, @Param("id") Long id);

    /**
     * Trobar categories per nom que contingui el text (cerca parcial, case insensitive)
     */
    List<Categoria> findByNomContainingIgnoreCase(String nom);

    /**
     * Trobar categories amb descripció que contingui el text
     */
    List<Categoria> findByDescripcioContainingIgnoreCase(String descripcio);

    /**
     * Trobar totes les categories amb els seus productes carregats (fetch join)
     */
    @Query("SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.productes")
    List<Categoria> findAllWithProducts();

    /**
     * Trobar productes d'una categoria específica
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId")
    List<Producte> findProductesByCategoria(@Param("categoriaId") Long categoriaId);

    /**
     * Trobar productes disponibles d'una categoria (stock > 0)
     */
    @Query("SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.stock > 0")
    List<Producte> findAvailableProductesByCategoria(@Param("categoriaId") Long categoriaId);

    /**
     * Comptar categories que tenen productes
     */
    @Query("SELECT c.id, c.nom, COUNT(p.id) FROM Categoria c LEFT JOIN c.productes p GROUP BY c.id, c.nom HAVING COUNT(p.id) > 0")
    List<Object[]> countCategoriesWithProducts();

    /**
     * Trobar les 5 categories amb més productes
     */
    @Query("SELECT c.id, c.nom, COUNT(p.id) as productCount FROM Categoria c LEFT JOIN c.productes p GROUP BY c.id, c.nom ORDER BY COUNT(p.id) DESC LIMIT 5")
    List<Object[]> findTop5CategoriesWithMostProducts();

    /**
     * Trobar categories que tenen productes amb stock disponible
     */
    @Query("SELECT DISTINCT c FROM Categoria c INNER JOIN c.productes p WHERE p.stock > 0")
    List<Categoria> findCategoriesWithAvailableProducts();

    /**
     * Comptar productes per categoria
     */
    @Query("SELECT COUNT(p) FROM Producte p WHERE p.categoria.id = :categoriaId")
    long countProductesByCategoria(@Param("categoriaId") Long categoriaId);

    /**
     * Trobar categories ordenades per nom
     */
    List<Categoria> findAllByOrderByNomAsc();

    /**
     * Trobar categories que tenen productes actius (no eliminats)
     */
    @Query("SELECT DISTINCT c FROM Categoria c INNER JOIN c.productes p WHERE p.actiu = true")
    List<Categoria> findCategoriesWithActiveProducts();

    /**
     * Verificar si una categoria té productes associats
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producte p WHERE p.categoria.id = :categoriaId")
    boolean hasProducts(@Param("categoriaId") Long categoriaId);

    /**
     * Obtenir estadístiques de productes per categoria
     */
    @Query("SELECT c.nom, COUNT(p.id), AVG(p.preu), MIN(p.preu), MAX(p.preu) FROM Categoria c LEFT JOIN c.productes p GROUP BY c.id, c.nom")
    List<Object[]> getProductStatsByCategory();
}