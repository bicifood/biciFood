package com.bicifood.api.controller;

import com.bicifood.api.model.Categoria;
import com.bicifood.api.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 🏷️ Controller REST per Categories
 * Gestiona les operacions CRUD per les categories de productes
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "API per gestionar categories de productes")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * 📋 Obtenir totes les categories
     */
    @GetMapping
    @Operation(summary = "Llistar totes les categories", 
               description = "Retorna la llista completa de categories ordenades per nom")
    public ResponseEntity<List<Categoria>> getAllCategories() {
        List<Categoria> categories = categoriaRepository.findAllOrderByNom();
        return ResponseEntity.ok(categories);
    }

    /**
     * 🔍 Obtenir categoria per ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir categoria per ID", 
               description = "Retorna una categoria específica segons el seu ID")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 🔍 Buscar categoria per nom
     */
    @GetMapping("/nom/{nom}")
    @Operation(summary = "Buscar categoria per nom", 
               description = "Busca una categoria pel seu nom exacte")
    public ResponseEntity<Categoria> getCategoriaByNom(@PathVariable String nom) {
        Optional<Categoria> categoria = categoriaRepository.findByNomCat(nom.toUpperCase());
        
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ➕ Crear nova categoria
     */
    @PostMapping
    @Operation(summary = "Crear nova categoria", 
               description = "Crea una nova categoria de productes")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        // Convertir nom a majúscules per consistència
        categoria.setNomCat(categoria.getNomCat().toUpperCase());
        
        // Verificar si ja existeix
        if (categoriaRepository.existsByNomCat(categoria.getNomCat())) {
            return ResponseEntity.badRequest().build();
        }
        
        Categoria novaCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(novaCategoria);
    }

    /**
     * ✏️ Actualitzar categoria
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar categoria", 
               description = "Actualitza una categoria existent")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, 
                                                     @RequestBody Categoria categoriaActualitzada) {
        Optional<Categoria> categoriaExistent = categoriaRepository.findById(id);
        
        if (categoriaExistent.isPresent()) {
            Categoria categoria = categoriaExistent.get();
            categoria.setNomCat(categoriaActualitzada.getNomCat().toUpperCase());
            
            Categoria categoriaGuardada = categoriaRepository.save(categoria);
            return ResponseEntity.ok(categoriaGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 🗑️ Eliminar categoria
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoria", 
               description = "Elimina una categoria (només si no té productes associats)")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        if (categoriaRepository.existsById(id)) {
            try {
                categoriaRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                // Error si té productes associats
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}