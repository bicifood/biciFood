package com.bicifood.api.repository;

import com.bicifood.api.entity.Comanda;
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
 * Repository per l'entitat Comanda
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Integer> {

    /**
     * Troba comandes per client
     * @param clientId l'ID del client
     * @param pageable paginació
     * @return pàgina de comandes del client
     */
    Page<Comanda> findByClientId(Integer clientId, Pageable pageable);

    /**
     * Troba comandes per client ordenades per data (més recents primer)
     * @param clientId l'ID del client
     * @return llista de comandes del client ordenades per data
     */
    @Query("SELECT c FROM Comanda c WHERE c.client.id = :clientId ORDER BY c.dataHoraComanda DESC")
    List<Comanda> findByClientIdOrderByDataHoraComandaDesc(@Param("clientId") Integer clientId);

    /**
     * Troba comandes per estat
     * @param estatId l'ID de l'estat
     * @param pageable paginació
     * @return pàgina de comandes amb aquest estat
     */
    Page<Comanda> findByEstatId(Integer estatId, Pageable pageable);

    /**
     * Troba comandes per nom de l'estat
     * @param nomEstat el nom de l'estat
     * @return llista de comandes amb aquest estat
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat.nom = :nomEstat")
    List<Comanda> findByEstatNom(@Param("nomEstat") String nomEstat);

    /**
     * Troba comandes pendents (estat PENDENT)
     * @return llista de comandes pendents
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat.nom = 'PENDENT' ORDER BY c.dataHoraComanda ASC")
    List<Comanda> findPendingOrders();

    /**
     * Troba comandes en preparació (estat PREPARANT)
     * @return llista de comandes en preparació
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat.nom = 'PREPARANT' ORDER BY c.dataHoraComanda ASC")
    List<Comanda> findPreparingOrders();

    /**
     * Troba comandes en ruta (estat EN RUTA)
     * @return llista de comandes en ruta
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat.nom = 'EN RUTA' ORDER BY c.dataHoraComanda ASC")
    List<Comanda> findOrdersInRoute();

    /**
     * Troba comandes lliurades (estat LLIURADA)
     * @param pageable paginació
     * @return pàgina de comandes lliurades
     */
    @Query("SELECT c FROM Comanda c WHERE c.estat.nom = 'LLIURADA' ORDER BY c.dataHoraComanda DESC")
    Page<Comanda> findDeliveredOrders(Pageable pageable);

    /**
     * Troba comandes per rang de dates
     * @param startDate data d'inici
     * @param endDate data de fi
     * @param pageable paginació
     * @return pàgina de comandes en el rang de dates
     */
    @Query("SELECT c FROM Comanda c WHERE c.dataHoraComanda BETWEEN :startDate AND :endDate ORDER BY c.dataHoraComanda DESC")
    Page<Comanda> findByDataHoraComandaBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate, 
                                               Pageable pageable);

    /**
     * Troba comandes per rang d'import
     * @param minImport import mínim
     * @param maxImport import màxim
     * @param pageable paginació
     * @return pàgina de comandes en el rang d'import
     */
    @Query("SELECT c FROM Comanda c WHERE c.importTotal BETWEEN :minImport AND :maxImport ORDER BY c.importTotal DESC")
    Page<Comanda> findByImportTotalBetween(@Param("minImport") BigDecimal minImport, 
                                           @Param("maxImport") BigDecimal maxImport, 
                                           Pageable pageable);

    /**
     * Troba comandes per codi postal de lliurament
     * @param codiPostal el codi postal
     * @return llista de comandes per aquest codi postal
     */
    List<Comanda> findByCpLliurament(String codiPostal);

    /**
     * Calcula l'import total de vendes per un client
     * @param clientId l'ID del client
     * @return suma total dels imports de les comandes del client
     */
    @Query("SELECT COALESCE(SUM(c.importTotal), 0) FROM Comanda c WHERE c.client.id = :clientId")
    BigDecimal calculateTotalSalesByClient(@Param("clientId") Integer clientId);

    /**
     * Calcula l'import total de vendes per un període
     * @param startDate data d'inici
     * @param endDate data de fi
     * @return suma total dels imports en el període
     */
    @Query("SELECT COALESCE(SUM(c.importTotal), 0) FROM Comanda c WHERE c.dataHoraComanda BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalSalesByPeriod(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Conta comandes per client
     * @param clientId l'ID del client
     * @return número de comandes del client
     */
    Long countByClientId(Integer clientId);

    /**
     * Trova les comandes més recents
     * @param limit número màxim de resultats
     * @return llista de les comandes més recents
     */
    @Query("SELECT c FROM Comanda c ORDER BY c.dataHoraComanda DESC")
    List<Comanda> findRecentOrders(Pageable pageable);

    /**
     * Troba comandes per client i estat
     * @param clientId l'ID del client
     * @param estatId l'ID de l'estat
     * @return llista de comandes del client amb l'estat especificat
     */
    @Query("SELECT c FROM Comanda c WHERE c.client.id = :clientId AND c.estat.id = :estatId ORDER BY c.dataHoraComanda DESC")
    List<Comanda> findByClientIdAndEstatId(@Param("clientId") Integer clientId, @Param("estatId") Integer estatId);
}