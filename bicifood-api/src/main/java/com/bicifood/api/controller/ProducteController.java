package com.bicifood.api.controller;

import com.bicifood.api.model.Producte;
import com.bicifood.api.repository.ProducteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 🍽️ Controller REST per Productes
 * Gestiona les operacions CRUD per els productes del menú
 */
@RestController
@RequestMapping("/api/v1/productes")
@Tag(name = "Productes", description = "API per gestionar productes del menú")
@CrossOrigin(origins = "*")
public class ProducteController {

    @Autowired
    private ProducteRepository producteRepository;

    /**
     * 📋 Obtenir tots els productes
     */
    @GetMapping
    @Operation(summary = "Llistar tots els productes", 
               description = "Retorna la llista completa de productes")
    public ResponseEntity<List<Producte>> getAllProductes() {
        List<Producte> productes = producteRepository.findAll();
        return ResponseEntity.ok(productes);
    }

    /**
     * 🔍 Obtenir producte per ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir producte per ID", 
               description = "Retorna un producte específic segons el seu ID")
    public ResponseEntity<Producte> getProducteById(@PathVariable Integer id) {
        Optional<Producte> producte = producteRepository.findById(id);
        
        if (producte.isPresent()) {
            return ResponseEntity.ok(producte.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 🏷️ Obtenir productes per categoria
     */
    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Productes per categoria", 
               description = "Retorna tots els productes d'una categoria específica")
    public ResponseEntity<List<Producte>> getProductesByCategoria(@PathVariable Integer idCategoria) {
        List<Producte> productes = producteRepository.findByCategoriaIdCategoria(idCategoria);
        return ResponseEntity.ok(productes);
    }

    /**
     * 🏷️ Obtenir productes per categoria amb paginació
     */
    @GetMapping("/categoria/{idCategoria}/paginated")
    @Operation(summary = "Productes per categoria (paginat)", 
               description = "Retorna productes d'una categoria amb paginació")
    public ResponseEntity<Page<Producte>> getProductesByCategoriaPageable(
            @PathVariable Integer idCategoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Producte> productes = producteRepository.findByCategoriaIdCategoria(idCategoria, pageable);
        return ResponseEntity.ok(productes);
    }

    /**
     * 🔍 Buscar productes per nom
     */
    @GetMapping("/cerca")
    @Operation(summary = "Buscar productes per nom", 
               description = "Busca productes que continguin el text especificat")
    public ResponseEntity<List<Producte>> searchProductes(@RequestParam String nom) {
        List<Producte> productes = producteRepository.findByNomContaining(nom);
        return ResponseEntity.ok(productes);
    }

    /**
     * ✅ Obtenir productes disponibles
     */
    @GetMapping("/disponibles")
    @Operation(summary = "Productes disponibles", 
               description = "Retorna només els productes amb stock disponible")
    public ResponseEntity<List<Producte>> getAvailableProductes() {
        List<Producte> productes = producteRepository.findAvailableProducts();
        return ResponseEntity.ok(productes);
    }

    /**
     * 💰 Buscar productes per rang de preu
     */
    @GetMapping("/preu")
    @Operation(summary = "Productes per rang de preu", 
               description = "Busca productes dins d'un rang de preus")
    public ResponseEntity<List<Producte>> getProductesByPreuRange(
            @RequestParam BigDecimal preuMin,
            @RequestParam BigDecimal preuMax) {
        
        List<Producte> productes = producteRepository.findByPreuBetween(preuMin, preuMax);
        return ResponseEntity.ok(productes);
    }

    /**
     * 🔥 Productes populars
     */
    @GetMapping("/populars")
    @Operation(summary = "Productes populars", 
               description = "Retorna els productes més populars")
    public ResponseEntity<List<Producte>> getPopularProductes() {
        List<Producte> productes = producteRepository.findPopularProducts();
        return ResponseEntity.ok(productes);
    }

    /**
     * ➕ Crear nou producte
     */
    @PostMapping
    @Operation(summary = "Crear nou producte", 
               description = "Crea un nou producte al menú")
    public ResponseEntity<Producte> createProducte(@RequestBody Producte producte) {
        // Verificar si ja existeix un producte amb aquest nom
        if (producteRepository.existsByNom(producte.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        
        Producte nouProducte = producteRepository.save(producte);
        return ResponseEntity.ok(nouProducte);
    }

    /**
     * ✏️ Actualitzar producte
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar producte", 
               description = "Actualitza un producte existent")
    public ResponseEntity<Producte> updateProducte(@PathVariable Integer id, 
                                                   @RequestBody Producte producteActualitzat) {
        Optional<Producte> producteExistent = producteRepository.findById(id);
        
        if (producteExistent.isPresent()) {
            Producte producte = producteExistent.get();
            producte.setNom(producteActualitzat.getNom());
            producte.setPreu(producteActualitzat.getPreu());
            producte.setDescripcio(producteActualitzat.getDescripcio());
            producte.setStock(producteActualitzat.getStock());
            producte.setImatgePath(producteActualitzat.getImatgePath());
            producte.setCategoria(producteActualitzat.getCategoria());
            
            Producte producteGuardat = producteRepository.save(producte);
            return ResponseEntity.ok(producteGuardat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 🗑️ Eliminar producte
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producte", 
               description = "Elimina un producte del menú")
    public ResponseEntity<Void> deleteProducte(@PathVariable Integer id) {
        if (producteRepository.existsById(id)) {
            producteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}