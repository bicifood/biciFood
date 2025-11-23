package com.bicifood.api.repository;

import com.bicifood.api.entity.EstatComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per l'entitat EstatComanda
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface EstatComandaRepository extends JpaRepository<EstatComanda, Integer> {

    /**
     * Troba un estat de comanda pel seu nom
     * @param nom el nom de l'estat
     * @return Optional amb l'estat si existeix
     */
    Optional<EstatComanda> findByNom(String nom);

    /**
     * Comprova si existeix un estat amb aquest nom
     * @param nom el nom de l'estat
     * @return true si existeix, false altrament
     */
    boolean existsByNom(String nom);

    /**
     * Troba tots els estats ordenats per nom
     * @return llista d'estats ordenats per nom
     */
    @Query("SELECT e FROM EstatComanda e ORDER BY e.nom")
    List<EstatComanda> findAllOrderByNom();

    /**
     * Conta el número de comandes per estat
     * @param estatId l'ID de l'estat
     * @return número de comandes amb aquest estat
     */
    @Query("SELECT COUNT(c) FROM Comanda c WHERE c.estat.id = :estatId")
    Long countComandesByEstat(Integer estatId);
}