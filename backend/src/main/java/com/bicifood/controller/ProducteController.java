package com.bicifood.controller;

import com.bicifood.model.Producte;
import com.bicifood.service.ProducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller per gestionar productes
 * Proporciona endpoints per operacions CRUD sobre productes
 */
@RestController
@RequestMapping("/api/productes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class ProducteController {

    @Autowired
    private ProducteService producteService;

    /**
     * Obtenir tots els productes disponibles
     * GET /api/productes
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProductes() {
        try {
            List<Producte> productes = producteService.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productes);
            response.put("count", productes.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint productes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir productes per categoria
     * GET /api/productes?categoria={categoriaId}
     */
    @GetMapping(params = "categoria")
    public ResponseEntity<Map<String, Object>> getProductesByCategoria(@RequestParam Long categoria) {
        try {
            List<Producte> productes = producteService.findByCategoria(categoria);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productes);
            response.put("count", productes.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint productes per categoria: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir un producte específic per ID
     * GET /api/productes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProducteById(@PathVariable Long id) {
        try {
            Producte producte = producteService.findById(id);
            
            if (producte != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", producte);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Producte no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear un nou producte
     * POST /api/productes
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProducte(@Valid @RequestBody Producte producte) {
        try {
            Producte nouProducte = producteService.save(producte);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", nouProducte);
            response.put("message", "Producte creat correctament");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error creant producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Actualitzar un producte existent
     * PUT /api/productes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProducte(@PathVariable Long id, @Valid @RequestBody Producte producte) {
        try {
            Producte producteExistent = producteService.findById(id);
            
            if (producteExistent != null) {
                producte.setId(id);
                Producte producteActualitzat = producteService.save(producte);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", producteActualitzat);
                response.put("message", "Producte actualitzat correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Producte no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Eliminar un producte
     * DELETE /api/productes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProducte(@PathVariable Long id) {
        try {
            Producte producte = producteService.findById(id);
            
            if (producte != null) {
                producteService.delete(id);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Producte eliminat correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Producte no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error eliminant producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualitzar stock d'un producte
     * PATCH /api/productes/{id}/stock
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockData) {
        try {
            Integer nouStock = stockData.get("stock");
            if (nouStock == null || nouStock < 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Stock no vàlid");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            Producte producte = producteService.updateStock(id, nouStock);
            
            if (producte != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", producte);
                response.put("message", "Stock actualitzat correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Producte no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant stock: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Cercar productes per nom
     * GET /api/productes/cerca?q={query}
     */
    @GetMapping("/cerca")
    public ResponseEntity<Map<String, Object>> cercarProductes(@RequestParam String q) {
        try {
            List<Producte> productes = producteService.searchByNom(q);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productes);
            response.put("count", productes.size());
            response.put("query", q);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error cercant productes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}