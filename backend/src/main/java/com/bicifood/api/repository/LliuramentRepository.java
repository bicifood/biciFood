package com.bicifood.api.repository;

import com.bicifood.api.entity.Lliurament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository per l'entitat Lliurament
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface LliuramentRepository extends JpaRepository<Lliurament, Integer> {

    /**
     * Troba un lliurament per ID de comanda
     * @param comandaId l'ID de la comanda
     * @return Optional amb el lliurament si existeix
     */
    Optional<Lliurament> findByComandaId(Integer comandaId);

    /**
     * Troba lliuraments per repartidor
     * @param repartidorId l'ID del repartidor
     * @param pageable paginació
     * @return pàgina de lliuraments del repartidor
     */
    Page<Lliurament> findByRepartidorId(Integer repartidorId, Pageable pageable);

    /**
     * Trova lliuraments assignats a un repartidor ordenats per data d'assignació
     * @param repartidorId l'ID del repartidor
     * @return llista de lliuraments del repartidor ordenats per data
     */
    @Query("SELECT l FROM Lliurament l WHERE l.repartidor.id = :repartidorId ORDER BY l.dataHoraAssignacio DESC")
    List<Lliurament> findByRepartidorIdOrderByDataHoraAssignacioDesc(@Param("repartidorId") Integer repartidorId);

    /**
     * Troba lliuraments sense assignar (sense repartidor)
     * @return llista de lliuraments sense assignar
     */
    @Query("SELECT l FROM Lliurament l WHERE l.repartidor IS NULL ORDER BY l.dataHoraAssignacio ASC")
    List<Lliurament> findUnassignedDeliveries();

    /**
     * Trova lliuraments pendents (assignats però no lliurats)
     * @param repartidorId l'ID del repartidor (opcional)
     * @return llista de lliuraments pendents
     */
    @Query("SELECT l FROM Lliurament l WHERE l.dataHoraLliuramentReal IS NULL " +
           "AND (:repartidorId IS NULL OR l.repartidor.id = :repartidorId) " +
           "ORDER BY l.dataHoraAssignacio ASC")
    List<Lliurament> findPendingDeliveries(@Param("repartidorId") Integer repartidorId);

    /**
     * Trova lliuraments completats (amb data de lliurament)
     * @param pageable paginació
     * @return pàgina de lliuraments completats
     */
    @Query("SELECT l FROM Lliurament l WHERE l.dataHoraLliuramentReal IS NOT NULL ORDER BY l.dataHoraLliuramentReal DESC")
    Page<Lliurament> findCompletedDeliveries(Pageable pageable);

    /**
     * Trova lliuraments completats per repartidor
     * @param repartidorId l'ID del repartidor
     * @param pageable paginació
     * @return pàgina de lliuraments completats del repartidor
     */
    @Query("SELECT l FROM Lliurament l WHERE l.repartidor.id = :repartidorId AND l.dataHoraLliuramentReal IS NOT NULL " +
           "ORDER BY l.dataHoraLliuramentReal DESC")
    Page<Lliurament> findCompletedDeliveriesByRepartidor(@Param("repartidorId") Integer repartidorId, Pageable pageable);

    /**
     * Trova lliuraments per rang de dates d'assignació
     * @param startDate data d'inici
     * @param endDate data de fi
     * @param pageable paginació
     * @return pàgina de lliuraments en el rang de dates
     */
    @Query("SELECT l FROM Lliurament l WHERE l.dataHoraAssignacio BETWEEN :startDate AND :endDate " +
           "ORDER BY l.dataHoraAssignacio DESC")
    Page<Lliurament> findByDataHoraAssignacionBetween(@Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate, 
                                                      Pageable pageable);

    /**
     * Trova lliuraments per rang de dates de lliurament real
     * @param startDate data d'inici
     * @param endDate data de fi
     * @param pageable paginació
     * @return pàgina de lliuraments lliurats en el rang de dates
     */
    @Query("SELECT l FROM Lliurament l WHERE l.dataHoraLliuramentReal BETWEEN :startDate AND :endDate " +
           "ORDER BY l.dataHoraLliuramentReal DESC")
    Page<Lliurament> findByDataHoraLliuramentRealBetween(@Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate, 
                                                         Pageable pageable);

    /**
     * Conta lliuraments completats per repartidor
     * @param repartidorId l'ID del repartidor
     * @return número de lliuraments completats
     */
    @Query("SELECT COUNT(l) FROM Lliurament l WHERE l.repartidor.id = :repartidorId AND l.dataHoraLliuramentReal IS NOT NULL")
    Long countCompletedDeliveriesByRepartidor(@Param("repartidorId") Integer repartidorId);

    /**
     * Conta lliuraments pendents per repartidor
     * @param repartidorId l'ID del repartidor
     * @return número de lliuraments pendents
     */
    @Query("SELECT COUNT(l) FROM Lliurament l WHERE l.repartidor.id = :repartidorId AND l.dataHoraLliuramentReal IS NULL")
    Long countPendingDeliveriesByRepartidor(@Param("repartidorId") Integer repartidorId);

    /**
     * Calcula el temps mitjà de lliurament (des de l'assignació fins al lliurament)
     * @param repartidorId l'ID del repartidor (opcional)
     * @return temps mitjà en minuts
     */
    @Query("SELECT AVG(EXTRACT(EPOCH FROM (l.dataHoraLliuramentReal - l.dataHoraAssignacio))/60) " +
           "FROM Lliurament l WHERE l.dataHoraLliuramentReal IS NOT NULL " +
           "AND (:repartidorId IS NULL OR l.repartidor.id = :repartidorId)")
    Double calculateAverageDeliveryTime(@Param("repartidorId") Integer repartidorId);

    /**
     * Troba lliuraments per codi postal
     * @param codiPostal el codi postal de lliurament
     * @return llista de lliuraments per aquest codi postal
     */
    @Query("SELECT l FROM Lliurament l WHERE l.comanda.cpLliurament = :codiPostal")
    List<Lliurament> findByCodiPostal(@Param("codiPostal") String codiPostal);

    /**
     * Comprova si existeix un lliurament per una comanda
     * @param comandaId l'ID de la comanda
     * @return true si existeix, false altrament
     */
    boolean existsByComandaId(Integer comandaId);
}