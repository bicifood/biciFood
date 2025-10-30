package com.bicifood.repository;

import com.bicifood.model.Comanda;
import com.bicifood.model.EstatComanda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository per gestionar comandes a la base de dades
 * Utilitza Spring Data JPA per operacions CRUD i consultes personalitzades
 */
@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    /**
     * Trobar comandes per usuari amb paginació
     */
    Page<Comanda> findByUsuariId(Long usuariId, Pageable pageable);

    /**
     * Trobar comandes per usuari i estat amb paginació
     */
    Page<Comanda> findByUsuariIdAndEstat(Long usuariId, EstatComanda estat, Pageable pageable);

    /**
     * Trobar comandes per estat amb paginació
     */
    Page<Comanda> findByEstat(EstatComanda estat, Pageable pageable);

    /**
     * Trobar comandes per usuari ordenades per data
     */
    List<Comanda> findByUsuariIdOrderByDataComandaDesc(Long usuariId);

    /**
     * Comptar comandes per usuari
     */
    long countByUsuariId(Long usuariId);

    /**
     * Comptar comandes per usuari i data després de
     */
    long countByUsuariIdAndDataComandaAfter(Long usuariId, LocalDateTime data);

    /**
     * Comptar comandes per data després de
     */
    long countByDataComandaAfter(LocalDateTime data);

    /**
     * Comptar comandes per estat
     */
    long countByEstat(EstatComanda estat);

    /**
     * Suma total per usuari
     */
    @Query("SELECT SUM(c.preuTotal) FROM Comanda c WHERE c.usuari.id = :usuariId")
    BigDecimal sumTotalByUsuariId(@Param("usuariId") Long usuariId);

    /**
     * Suma total de totes les comandes
     */
    @Query("SELECT SUM(c.preuTotal) FROM Comanda c")
    BigDecimal sumTotalAll();

    /**
     * Trobar comandes per rang de dates
     */
    List<Comanda> findByDataComandaBetween(LocalDateTime dataInici, LocalDateTime dataFi);

    /**
     * Trobar comandes actives (no cancel·lades ni entregades)
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat NOT IN ('CANCEL_LADA', 'ENTREGADA')")
    List<Comanda> findActiveComandes();

    /**
     * Trobar comandes pendents per usuari
     */
    List<Comanda> findByUsuariIdAndEstatIn(Long usuariId, List<EstatComanda> estats);

    /**
     * Obtenir estadístiques de comandes per dia
     */
    @Query("SELECT DATE(c.dataComanda) as dia, COUNT(c) as total, SUM(c.preuTotal) as import FROM Comanda c GROUP BY DATE(c.dataComanda) ORDER BY dia DESC")
    List<Object[]> getComandesStatsPerDia();

    /**
     * Trobar les últimes comandes d'un usuari
     */
    List<Comanda> findTop10ByUsuariIdOrderByDataComandaDesc(Long usuariId);

    /**
     * Trobar comandes per rang de dates amb paginació
     */
    Page<Comanda> findByDataComandaBetween(LocalDateTime dataInici, LocalDateTime dataFi, Pageable pageable);

    /**
     * Suma total per data després de
     */
    @Query("SELECT SUM(c.preuTotal) FROM Comanda c WHERE c.dataComanda >= :data")
    BigDecimal sumTotalByDataAfter(@Param("data") LocalDateTime data);
}