package com.bicifood.api.service;

import com.bicifood.api.entity.Categoria;
import com.bicifood.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servei per gestionar les operacions de Categoria
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obté totes les categories
     */
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Obté una categoria per ID
     */
    public Optional<Categoria> findById(Integer id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Guarda una categoria
     */
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Elimina una categoria per ID
     */
    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }

    /**
     * Comprova si existeix una categoria amb un nom específic
     */
    public boolean existsByNom(String nom) {
        return categoriaRepository.existsByNom(nom);
    }
}