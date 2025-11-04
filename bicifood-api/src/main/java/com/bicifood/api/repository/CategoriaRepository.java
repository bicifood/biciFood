package com.bicifood.api.repository;

import com.bicifood.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 🗄️ Repository per Categoria
 * Proporciona operacions CRUD i consultes personalitzades per categories
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Busca una categoria pel seu nom
     * @param nomCat Nom de la categoria
     * @return Optional amb la categoria si existeix
     */
    Optional<Categoria> findByNomCat(String nomCat);

    /**
     * Comprova si existeix una categoria amb aquest nom
     * @param nomCat Nom de la categoria
     * @return true si existeix, false altrament
     */
    boolean existsByNomCat(String nomCat);

    /**
     * Obté totes les categories ordenades per nom
     * @return Llista de categories ordenades
     */
    @Query("SELECT c FROM Categoria c ORDER BY c.nomCat ASC")
    java.util.List<Categoria> findAllOrderByNom();
}