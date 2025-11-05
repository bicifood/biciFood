package com.bicifood.api.repository;

import com.bicifood.api.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per l'entitat Rol
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Troba un rol pel seu nom
     * @param nom el nom del rol
     * @return Optional amb el rol si existeix
     */
    Optional<Rol> findByNom(String nom);

    /**
     * Comprova si existeix un rol amb aquest nom
     * @param nom el nom del rol
     * @return true si existeix, false altrament
     */
    boolean existsByNom(String nom);

    /**
     * Troba tots els rols ordenats per nom
     * @return llista de rols ordenats per nom
     */
    @Query("SELECT r FROM Rol r ORDER BY r.nom")
    java.util.List<Rol> findAllOrderByNom();
}