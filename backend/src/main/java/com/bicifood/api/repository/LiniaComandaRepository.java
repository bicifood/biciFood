package com.bicifood.api.repository;

import com.bicifood.api.entity.LiniaComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository per l'entitat LiniaComanda
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface LiniaComandaRepository extends JpaRepository<LiniaComanda, Integer> {

    /**
     * Trova línies de comanda per ID de comanda
     * @param comandaId l'ID de la comanda
     * @return llista de línies de la comanda
     */
    List<LiniaComanda> findByComandaId(Integer comandaId);

    /**
     * Trova línies de comanda per ID de producte
     * @param producteId l'ID del producte
     * @return llista de línies amb aquest producte
     */
    List<LiniaComanda> findByProducteId(Integer producteId);

    /**
     * Calcula el subtotal d'una comanda
     * @param comandaId l'ID de la comanda
     * @return suma dels subtotals de totes les línies de la comanda
     */
    @Query("SELECT COALESCE(SUM(lc.subtotal), 0) FROM LiniaComanda lc WHERE lc.comanda.id = :comandaId")
    BigDecimal calculateSubtotalByComanda(@Param("comandaId") Integer comandaId);

    /**
     * Calcula la quantitat total de productes venuts
     * @param producteId l'ID del producte
     * @return suma de totes les quantitats venudes d'aquest producte
     */
    @Query("SELECT COALESCE(SUM(lc.quantitat), 0) FROM LiniaComanda lc WHERE lc.producte.id = :producteId")
    Long calculateTotalQuantitySold(@Param("producteId") Integer producteId);

    /**
     * Troba els productes més venuts (per quantitat)
     * @param limit número màxim de resultats
     * @return llista de productes ordenats per quantitat venuda (descendent)
     */
    @Query("SELECT lc.producte FROM LiniaComanda lc " +
           "GROUP BY lc.producte " +
           "ORDER BY SUM(lc.quantitat) DESC")
    List<com.bicifood.api.entity.Producte> findBestSellingProductsByQuantity(org.springframework.data.domain.Pageable pageable);

    /**
     * Troba els productes que generen més ingressos
     * @param limit número màxim de resultats
     * @return llista de productes ordenats per ingressos generats (descendent)
     */
    @Query("SELECT lc.producte FROM LiniaComanda lc " +
           "GROUP BY lc.producte " +
           "ORDER BY SUM(lc.subtotal) DESC")
    List<com.bicifood.api.entity.Producte> findBestSellingProductsByRevenue(org.springframework.data.domain.Pageable pageable);

    /**
     * Calcula els ingressos totals generats per un producte
     * @param producteId l'ID del producte
     * @return suma dels subtotals de totes les línies amb aquest producte
     */
    @Query("SELECT COALESCE(SUM(lc.subtotal), 0) FROM LiniaComanda lc WHERE lc.producte.id = :producteId")
    BigDecimal calculateTotalRevenueByProduct(@Param("producteId") Integer producteId);

    /**
     * Trova línies de comanda per rang de preus unitaris
     * @param minPreu preu mínim
     * @param maxPreu preu màxim
     * @return llista de línies en el rang de preus
     */
    @Query("SELECT lc FROM LiniaComanda lc WHERE lc.preuUnitari BETWEEN :minPreu AND :maxPreu")
    List<LiniaComanda> findByPreuUnitariBetween(@Param("minPreu") BigDecimal minPreu, 
                                               @Param("maxPreu") BigDecimal maxPreu);

    /**
     * Trova línies de comanda amb quantitat superior al límit especificat
     * @param quantitat quantitat mínima
     * @return llista de línies amb quantitat superior
     */
    @Query("SELECT lc FROM LiniaComanda lc WHERE lc.quantitat >= :quantitat")
    List<LiniaComanda> findByQuantitatGreaterThanEqual(@Param("quantitat") Integer quantitat);

    /**
     * Conta el número de línies per comanda
     * @param comandaId l'ID de la comanda
     * @return número de línies en la comanda
     */
    Long countByComandaId(Integer comandaId);

    /**
     * Elimina totes les línies d'una comanda
     * @param comandaId l'ID de la comanda
     */
    void deleteByComandaId(Integer comandaId);

    /**
     * Troba línies de comanda d'un client específic
     * @param clientId l'ID del client
     * @return llista de línies de comandes del client
     */
    @Query("SELECT lc FROM LiniaComanda lc WHERE lc.comanda.client.id = :clientId")
    List<LiniaComanda> findByClientId(@Param("clientId") Integer clientId);
}