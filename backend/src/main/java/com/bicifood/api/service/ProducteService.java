package com.bicifood.api.service;

import com.bicifood.api.entity.Producte;
import com.bicifood.api.entity.Categoria;
import com.bicifood.api.repository.ProducteRepository;
import com.bicifood.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestionar productes
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Service
@Transactional
public class ProducteService {

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Troba tots els productes
     * @param pageable paginació
     * @return pàgina de productes
     */
    @Transactional(readOnly = true)
    public Page<Producte> findAll(Pageable pageable) {
        return producteRepository.findAll(pageable);
    }

    /**
     * Troba un producte per ID
     * @param id l'ID del producte
     * @return Optional amb el producte si existeix
     */
    @Transactional(readOnly = true)
    public Optional<Producte> findById(Integer id) {
        return producteRepository.findById(id);
    }

    /**
     * Troba un producte per nom
     * @param nom el nom del producte
     * @return Optional amb el producte si existeix
     */
    @Transactional(readOnly = true)
    public Optional<Producte> findByNom(String nom) {
        return producteRepository.findByNom(nom);
    }

    /**
     * Crea un nou producte
     * @param producte el producte a crear
     * @return el producte creat
     * @throws RuntimeException si el nom ja existeix o la categoria no existeix
     */
    public Producte save(Producte producte) {
        if (producteRepository.existsByNom(producte.getNom())) {
            throw new RuntimeException("Ja existeix un producte amb aquest nom: " + producte.getNom());
        }

        // Verificar que la categoria existeix
        if (producte.getCategoria() == null || producte.getCategoria().getId() == null) {
            throw new RuntimeException("La categoria és obligatòria");
        }

        Categoria categoria = categoriaRepository.findById(producte.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria no trobada amb ID: " + producte.getCategoria().getId()));

        producte.setCategoria(categoria);

        // Inicialitzar stock si és null
        if (producte.getStock() == null) {
            producte.setStock(0);
        }

        return producteRepository.save(producte);
    }

    /**
     * Actualitza un producte existent
     * @param id l'ID del producte
     * @param producteActualitzat les dades actualitzades
     * @return el producte actualitzat
     * @throws RuntimeException si el producte no existeix
     */
    public Producte update(Integer id, Producte producteActualitzat) {
        Producte producteExistent = producteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat amb ID: " + id));

        // Verificar si el nom ha canviat i si ja existeix
        if (!producteExistent.getNom().equals(producteActualitzat.getNom()) &&
            producteRepository.existsByNom(producteActualitzat.getNom())) {
            throw new RuntimeException("Ja existeix un producte amb aquest nom: " + producteActualitzat.getNom());
        }

        // Verificar categoria si s'ha canviat
        if (producteActualitzat.getCategoria() != null && producteActualitzat.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producteActualitzat.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria no trobada amb ID: " + producteActualitzat.getCategoria().getId()));
            producteExistent.setCategoria(categoria);
        }

        // Actualitzar camps
        producteExistent.setNom(producteActualitzat.getNom());
        producteExistent.setPreu(producteActualitzat.getPreu());
        producteExistent.setDescripcio(producteActualitzat.getDescripcio());
        producteExistent.setImatgePath(producteActualitzat.getImatgePath());
        
        if (producteActualitzat.getStock() != null) {
            producteExistent.setStock(producteActualitzat.getStock());
        }

        return producteRepository.save(producteExistent);
    }

    /**
     * Elimina un producte
     * @param id l'ID del producte a eliminar
     * @throws RuntimeException si el producte no existeix
     */
    public void deleteById(Integer id) {
        if (!producteRepository.existsById(id)) {
            throw new RuntimeException("Producte no trobat amb ID: " + id);
        }
        producteRepository.deleteById(id);
    }

    /**
     * Troba productes per categoria
     * @param categoriaId l'ID de la categoria
     * @param pageable paginació
     * @return pàgina de productes de la categoria
     */
    @Transactional(readOnly = true)
    public Page<Producte> findByCategoria(Integer categoriaId, Pageable pageable) {
        return producteRepository.findByCategoriaId(categoriaId, pageable);
    }

    /**
     * Cerca productes per nom
     * @param nom part del nom del producte
     * @param pageable paginació
     * @return pàgina de productes que coincideixen
     */
    @Transactional(readOnly = true)
    public Page<Producte> searchByNom(String nom, Pageable pageable) {
        return producteRepository.findByNomContainingIgnoreCase(nom, pageable);
    }

    /**
     * Troba productes disponibles (amb stock > 0)
     * @return llista de productes disponibles
     */
    @Transactional(readOnly = true)
    public List<Producte> findAvailableProducts() {
        return producteRepository.findAvailableProducts();
    }

    /**
     * Troba productes amb stock baix
     * @param limit el límit de stock
     * @return llista de productes amb stock baix
     */
    @Transactional(readOnly = true)
    public List<Producte> findLowStockProducts(Integer limit) {
        return producteRepository.findLowStockProducts(limit);
    }

    /**
     * Troba productes sense stock
     * @return llista de productes sense stock
     */
    @Transactional(readOnly = true)
    public List<Producte> findOutOfStockProducts() {
        return producteRepository.findOutOfStockProducts();
    }

    /**
     * Troba productes per rang de preus
     * @param minPreu preu mínim
     * @param maxPreu preu màxim
     * @param pageable paginació
     * @return pàgina de productes en el rang de preus
     */
    @Transactional(readOnly = true)
    public Page<Producte> findByPreuRange(BigDecimal minPreu, BigDecimal maxPreu, Pageable pageable) {
        return producteRepository.findByPreuBetween(minPreu, maxPreu, pageable);
    }

    /**
     * Troba productes ordenats per preu
     * @param ascending true per ordre ascendent, false per descendent
     * @param pageable paginació
     * @return pàgina de productes ordenats per preu
     */
    @Transactional(readOnly = true)
    public Page<Producte> findAllOrderByPreu(boolean ascending, Pageable pageable) {
        return ascending ? 
               producteRepository.findAllByOrderByPreuAsc(pageable) : 
               producteRepository.findAllByOrderByPreuDesc(pageable);
    }

    /**
     * Cerca productes globalment (nom i descripció)
     * @param searchTerm terme de cerca
     * @param pageable paginació
     * @return pàgina de productes que coincideixen
     */
    @Transactional(readOnly = true)
    public Page<Producte> searchProducts(String searchTerm, Pageable pageable) {
        return producteRepository.searchProducts(searchTerm, pageable);
    }

    /**
     * Actualitza l'stock d'un producte
     * @param id l'ID del producte
     * @param nouStock el nou stock
     * @return el producte actualitzat
     */
    public Producte updateStock(Integer id, Integer nouStock) {
        Producte producte = producteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat amb ID: " + id));

        if (nouStock < 0) {
            throw new RuntimeException("L'stock no pot ser negatiu");
        }

        producte.setStock(nouStock);
        return producteRepository.save(producte);
    }

    /**
     * Redueix l'stock d'un producte (per vendes)
     * @param id l'ID del producte
     * @param quantitat la quantitat a restar
     * @return el producte actualitzat
     */
    public Producte reduirStock(Integer id, Integer quantitat) {
        Producte producte = producteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat amb ID: " + id));

        Integer stockActual = producte.getStock();
        if (stockActual < quantitat) {
            throw new RuntimeException("Stock insuficient. Disponible: " + stockActual + ", Sol·licitat: " + quantitat);
        }

        producte.setStock(stockActual - quantitat);
        return producteRepository.save(producte);
    }

    /**
     * Troba productes més populars
     * @param pageable paginació
     * @return llista de productes més populars
     */
    @Transactional(readOnly = true)
    public List<Producte> findMostPopularProducts(Pageable pageable) {
        return producteRepository.findMostPopularProducts(pageable);
    }

    /**
     * Troba productes disponibles per categoria
     * @param categoriaId l'ID de la categoria
     * @return llista de productes disponibles de la categoria
     */
    @Transactional(readOnly = true)
    public List<Producte> findAvailableProductsByCategory(Integer categoriaId) {
        return producteRepository.findAvailableProductsByCategory(categoriaId);
    }

    /**
     * Comprova si existeix un producte amb un nom determinat
     * @param nom el nom a verificar
     * @return true si existeix, false altrament
     */
    @Transactional(readOnly = true)
    public boolean existsByNom(String nom) {
        return producteRepository.existsByNom(nom);
    }
}