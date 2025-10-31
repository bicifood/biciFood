package com.bicifood.service.impl;

import com.bicifood.model.Producte;
import com.bicifood.repository.ProducteRepository;
import com.bicifood.service.ProducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementació del servei per gestionar productes
 * Conté la lògica de negoci per a productes
 */
@Service
@Transactional
public class ProducteServiceImpl implements ProducteService {

    @Autowired
    private ProducteRepository producteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producte> findAll() {
        return producteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producte> findByCategoria(Long categoriaId) {
        return producteRepository.findByCategoriaId(categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Producte findById(Long id) {
        return producteRepository.findById(id).orElse(null);
    }

    @Override
    public Producte save(Producte producte) {
        // Validacions de negoci
        if (producte.getPreu().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El preu ha de ser positiu");
        }
        
        if (producte.getStock() < 0) {
            throw new IllegalArgumentException("L'stock no pot ser negatiu");
        }
        
        return producteRepository.save(producte);
    }

    @Override
    public void delete(Long id) {
        Producte producte = findById(id);
        if (producte != null) {
            // Soft delete - marcar com inactiu en lloc d'eliminar
            producte.setActiu(false);
            producteRepository.save(producte);
        } else {
            throw new IllegalArgumentException("Producte no trobat amb id: " + id);
        }
    }

    @Override
    public Producte updateStock(Long id, Integer nouStock) {
        Producte producte = findById(id);
        if (producte != null) {
            if (nouStock < 0) {
                throw new IllegalArgumentException("L'stock no pot ser negatiu");
            }
            producte.setStock(nouStock);
            return producteRepository.save(producte);
        } else {
            throw new IllegalArgumentException("Producte no trobat amb id: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producte> searchByNom(String nom) {
        return producteRepository.findByNomContainingIgnoreCaseAndActiuTrue(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producte> findAvailable() {
        return producteRepository.findByStockGreaterThanAndActiuTrueOrderByNom(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producte> findDestacats() {
        return producteRepository.findByDestacatTrueAndActiuTrueOrderByNom();
    }
}