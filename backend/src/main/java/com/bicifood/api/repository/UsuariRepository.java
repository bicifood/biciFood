package com.bicifood.api.repository;

import com.bicifood.api.entity.Usuari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per l'entitat Usuari
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Integer> {

    /**
     * Troba un usuari pel seu email
     * @param email l'email de l'usuari
     * @return Optional amb l'usuari si existeix
     */
    Optional<Usuari> findByEmail(String email);

    /**
     * Comprova si existeix un usuari amb aquest email
     * @param email l'email de l'usuari
     * @return true si existeix, false altrament
     */
    boolean existsByEmail(String email);

    /**
     * Troba usuaris pel rol
     * @param rolId l'ID del rol
     * @return llista d'usuaris amb aquest rol
     */
    @Query("SELECT u FROM Usuari u WHERE u.rol.id = :rolId")
    List<Usuari> findByRolId(@Param("rolId") Integer rolId);

    /**
     * Troba usuaris pel nom del rol
     * @param nomRol el nom del rol
     * @return llista d'usuaris amb aquest rol
     */
    @Query("SELECT u FROM Usuari u WHERE u.rol.nom = :nomRol")
    List<Usuari> findByRolNom(@Param("nomRol") String nomRol);

    /**
     * Troba tots els clients (usuaris amb rol CLIENT)
     * @return llista de clients
     */
    @Query("SELECT u FROM Usuari u WHERE u.rol.nom = 'CLIENT'")
    List<Usuari> findAllClients();

    /**
     * Troba tots els repartidors (usuaris amb rol REPARTIDOR)
     * @return llista de repartidors
     */
    @Query("SELECT u FROM Usuari u WHERE u.rol.nom = 'REPARTIDOR'")
    List<Usuari> findAllRepartidors();

    /**
     * Troba usuaris per nom complet (cerca parcial, case-insensitive)
     * @param nomComplet el nom complet o part d'ell
     * @param pageable paginació
     * @return pàgina d'usuaris que coincideixen
     */
    @Query("SELECT u FROM Usuari u WHERE LOWER(u.nomComplet) LIKE LOWER(CONCAT('%', :nomComplet, '%'))")
    Page<Usuari> findByNomCompletContainingIgnoreCase(@Param("nomComplet") String nomComplet, Pageable pageable);

    /**
     * Troba usuaris per població
     * @param poblacio la població
     * @return llista d'usuaris d'aquesta població
     */
    List<Usuari> findByPoblacio(String poblacio);

    /**
     * Troba usuaris per codi postal
     * @param codiPostal el codi postal
     * @return llista d'usuaris d'aquest codi postal
     */
    List<Usuari> findByCodiPostal(String codiPostal);

    /**
     * Troba usuaris amb més punts que el valor especificat
     * @param punts el número mínim de punts
     * @return llista d'usuaris amb més punts
     */
    @Query("SELECT u FROM Usuari u WHERE u.punts >= :punts ORDER BY u.punts DESC")
    List<Usuari> findByPuntsGreaterThanEqualOrderByPuntsDesc(@Param("punts") Integer punts);

    /**
     * Compte el número d'usuaris per rol
     * @param rolId l'ID del rol
     * @return número d'usuaris amb aquest rol
     */
    @Query("SELECT COUNT(u) FROM Usuari u WHERE u.rol.id = :rolId")
    Long countByRolId(@Param("rolId") Integer rolId);
}