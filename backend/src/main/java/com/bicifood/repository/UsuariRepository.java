package com.bicifood.repository;

import com.bicifood.model.Usuari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per gestionar usuaris a la base de dades
 * Utilitza Spring Data JPA per operacions CRUD i consultes personalitzades
 */
@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Long> {

    /**
     * Trobar usuari per email
     */
    Optional<Usuari> findByEmail(String email);

    /**
     * Verificar si existeix un email
     */
    boolean existsByEmail(String email);

    /**
     * Trobar usuaris per nom que contingui el text
     */
    List<Usuari> findByNomContainingIgnoreCase(String nom);

    /**
     * Trobar usuaris per cognom que contingui el text
     */
    List<Usuari> findByCognomContainingIgnoreCase(String cognom);

    /**
     * Trobar usuaris actius
     */
    List<Usuari> findByActiuTrue();

    /**
     * Trobar usuaris per rol
     */
    List<Usuari> findByRol(Usuari.RolUsuari rol);

    /**
     * Trobar usuaris per rol amb paginació
     */
    Page<Usuari> findByRol(Usuari.RolUsuari rol, Pageable pageable);

    /**
     * Trobar usuaris per nom amb paginació
     */
    Page<Usuari> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    /**
     * Trobar usuaris amb comandes
     */
    @Query("SELECT DISTINCT u FROM Usuari u INNER JOIN u.comandes c")
    List<Usuari> findUsersWithComandes();

    /**
     * Comptar usuaris per rol
     */
    long countByRol(Usuari.RolUsuari rol);

    /**
     * Trobar usuaris registrats recentment (últims 7 dies)
     */
    @Query("SELECT u FROM Usuari u WHERE u.dataRegistre >= :sevenDaysAgo")
    List<Usuari> findRecentRegisteredUsers(@Param("sevenDaysAgo") java.time.LocalDateTime sevenDaysAgo);

    /**
     * Trobar usuaris per estat actiu amb paginació
     */
    Page<Usuari> findByActiu(Boolean actiu, Pageable pageable);

    /**
     * Trobar usuaris per rol i estat actiu amb paginació
     */
    Page<Usuari> findByRolAndActiu(Usuari.RolUsuari rol, Boolean actiu, Pageable pageable);
}