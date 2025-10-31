package com.bicifood.controller;

import com.bicifood.model.Categoria;
import com.bicifood.model.Producte;
import com.bicifood.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller per gestionar categories
 * Proporciona endpoints per operacions CRUD sobre categories
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Obtenir totes les categories
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<Categoria> categories = categoriaService.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", categories);
            response.put("count", categories.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint categories: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir categories amb els seus productes
     * GET /api/categories?with_products=true
     */
    @GetMapping(params = "with_products")
    public ResponseEntity<Map<String, Object>> getCategoriesWithProducts(@RequestParam("with_products") boolean withProducts) {
        try {
            List<Categoria> categories;
            
            if (withProducts) {
                categories = categoriaService.findAllWithProducts();
            } else {
                categories = categoriaService.findAll();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", categories);
            response.put("count", categories.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint categories amb productes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir una categoria específica per ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoriaById(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.findById(id);
            
            if (categoria != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", categoria);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Categoria no trobada");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir productes d'una categoria específica
     * GET /api/categories/{id}/productes
     */
    @GetMapping("/{id}/productes")
    public ResponseEntity<Map<String, Object>> getProductesByCategoria(@PathVariable Long id) {
        try {
            List<Producte> productes = categoriaService.getProductesByCategoria(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productes);
            response.put("count", productes.size());
            response.put("categoria_id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint productes de la categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear una nova categoria
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria novaCategoria = categoriaService.save(categoria);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", novaCategoria);
            response.put("message", "Categoria creada correctament");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error creant categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Actualitzar una categoria existent
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        try {
            Categoria categoriaExistent = categoriaService.findById(id);
            
            if (categoriaExistent != null) {
                categoria.setId(id);
                Categoria categoriaActualitzada = categoriaService.save(categoria);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", categoriaActualitzada);
                response.put("message", "Categoria actualitzada correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Categoria no trobada");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Eliminar una categoria
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategoria(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.findById(id);
            
            if (categoria != null) {
                // Verificar que no tingui productes associats
                List<Producte> productes = categoriaService.getProductesByCategoria(id);
                if (!productes.isEmpty()) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("error", "No es pot eliminar la categoria perquè té productes associats");
                    
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
                }
                
                categoriaService.delete(id);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Categoria eliminada correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Categoria no trobada");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error eliminant categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Estadístiques de categories
     * GET /api/categories/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCategoriesStats() {
        try {
            Map<String, Object> stats = categoriaService.getStats();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint estadístiques: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}