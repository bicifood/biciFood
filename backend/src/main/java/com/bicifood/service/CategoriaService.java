package com.bicifood.service;

import com.bicifood.model.Categoria;
import com.bicifood.model.Producte;
import com.bicifood.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servei per gestionar categories
 * Capa de lògica de negoci entre controller i repository
 */
@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Trobar totes les categories
     */
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Trobar totes les categories amb els seus productes carregats
     */
    public List<Categoria> findAllWithProducts() {
        return categoriaRepository.findAllWithProducts();
    }

    /**
     * Trobar categoria per ID
     */
    public Categoria findById(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.orElse(null);
    }

    /**
     * Guardar o actualitzar categoria
     */
    public Categoria save(Categoria categoria) {
        // Validacions de negoci si cal
        if (categoria.getNom() == null || categoria.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("El nom de la categoria és obligatori");
        }
        
        // Verificar que no existeixi una categoria amb el mateix nom
        if (categoria.getId() == null) {
            List<Categoria> existents = categoriaRepository.findByNom(categoria.getNom().trim());
            if (!existents.isEmpty()) {
                throw new IllegalArgumentException("Ja existeix una categoria amb aquest nom");
            }
        } else {
            List<Categoria> existents = categoriaRepository.findByNomAndIdNot(categoria.getNom().trim(), categoria.getId());
            if (!existents.isEmpty()) {
                throw new IllegalArgumentException("Ja existeix una categoria amb aquest nom");
            }
        }
        
        return categoriaRepository.save(categoria);
    }

    /**
     * Eliminar categoria per ID
     */
    public void delete(Long id) {
        // Verificar que existeixi
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("La categoria no existeix");
        }
        
        // Verificar que no tingui productes associats
        List<Producte> productes = getProductesByCategoria(id);
        if (!productes.isEmpty()) {
            throw new IllegalStateException("No es pot eliminar la categoria perquè té productes associats");
        }
        
        categoriaRepository.deleteById(id);
    }

    /**
     * Obtenir productes d'una categoria
     */
    public List<Producte> getProductesByCategoria(Long categoriaId) {
        Categoria categoria = findById(categoriaId);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no trobada");
        }
        
        return categoriaRepository.findProductesByCategoria(categoriaId);
    }

    /**
     * Verificar si una categoria existeix
     */
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    /**
     * Trobar categories per nom (cerca parcial)
     */
    public List<Categoria> findByNomContaining(String nom) {
        return categoriaRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Obtenir estadístiques de categories
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total de categories
        long totalCategories = categoriaRepository.count();
        stats.put("total_categories", totalCategories);
        
        // Categories amb productes
        List<Object[]> categoriesAmbProductes = categoriaRepository.countCategoriesWithProducts();
        stats.put("categories_amb_productes", categoriesAmbProductes.size());
        
        // Categories sense productes
        stats.put("categories_sense_productes", totalCategories - categoriesAmbProductes.size());
        
        // Top 5 categories amb més productes
        List<Object[]> topCategories = categoriaRepository.findTop5CategoriesWithMostProducts();
        stats.put("top_categories_amb_mes_productes", topCategories);
        
        return stats;
    }

    /**
     * Obtenir categories actives (que tenen productes disponibles)
     */
    public List<Categoria> findActiveCategories() {
        return categoriaRepository.findCategoriesWithAvailableProducts();
    }
}