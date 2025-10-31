package com.bicifood.repository;

import com.bicifood.model.DetallComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository per gestionar detalls de comanda a la base de dades
 * Utilitza Spring Data JPA per operacions CRUD i consultes personalitzades
 */
@Repository
public interface DetallComandaRepository extends JpaRepository<DetallComanda, Long> {

    /**
     * Trobar detalls per comanda
     */
    List<DetallComanda> findByComandaId(Long comandaId);

    /**
     * Trobar detalls per producte
     */
    List<DetallComanda> findByProducteId(Long producteId);

    /**
     * Calcular total de detalls per comanda
     */
    @Query("SELECT SUM(d.preuTotal) FROM DetallComanda d WHERE d.comanda.id = :comandaId")
    BigDecimal calculateTotalByComandaId(@Param("comandaId") Long comandaId);

    /**
     * Trobar productes més venuts
     */
    @Query("SELECT d.producte.id, d.producte.nom, SUM(d.quantitat) as totalVenuts " +
           "FROM DetallComanda d " +
           "GROUP BY d.producte.id, d.producte.nom " +
           "ORDER BY totalVenuts DESC")
    List<Object[]> findMostSoldProducts();

    /**
     * Comptar vendes d'un producte
     */
    @Query("SELECT SUM(d.quantitat) FROM DetallComanda d WHERE d.producte.id = :producteId")
    Long countSalesByProducteId(@Param("producteId") Long producteId);

    /**
     * Trobar detalls amb quantitat específica
     */
    List<DetallComanda> findByQuantitat(Integer quantitat);

    /**
     * Trobar detalls amb preu total major que un valor
     */
    List<DetallComanda> findByPreuTotalGreaterThan(BigDecimal preuTotal);
}