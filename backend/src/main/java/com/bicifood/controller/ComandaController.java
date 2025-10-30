package com.bicifood.controller;

import com.bicifood.model.Comanda;
import com.bicifood.model.DetallComanda;
import com.bicifood.model.EstatComanda;
import com.bicifood.service.ComandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller per gestionar comandes
 * Proporciona endpoints per operacions sobre comandes i el seu cicle de vida
 */
@RestController
@RequestMapping("/api/comandes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class ComandaController {

    @Autowired
    private ComandaService comandaService;

    /**
     * Obtenir totes les comandes (amb paginació)
     * GET /api/comandes?page=0&size=10&usuari_id=1
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllComandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long usuari_id,
            @RequestParam(required = false) EstatComanda estat) {
        try {
            Map<String, Object> result = comandaService.findAllWithPagination(page, size, usuari_id, estat);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result.get("comandes"));
            response.put("pagination", Map.of(
                "current_page", page,
                "page_size", size,
                "total_elements", result.get("totalElements"),
                "total_pages", result.get("totalPages")
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint comandes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir una comanda específica per ID
     * GET /api/comandes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getComandaById(@PathVariable Long id) {
        try {
            Comanda comanda = comandaService.findById(id);
            
            if (comanda != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", comanda);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Comanda no trobada");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint comanda: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir comandes d'un usuari específic
     * GET /api/comandes/usuari/{usuariId}
     */
    @GetMapping("/usuari/{usuariId}")
    public ResponseEntity<Map<String, Object>> getComandesByUsuari(@PathVariable Long usuariId) {
        try {
            List<Comanda> comandes = comandaService.findByUsuariId(usuariId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandes);
            response.put("count", comandes.size());
            response.put("usuari_id", usuariId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint comandes de l'usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear una nova comanda
     * POST /api/comandes
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComanda(@Valid @RequestBody Map<String, Object> comandaData) {
        try {
            Comanda novaComanda = comandaService.createComanda(comandaData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", novaComanda);
            response.put("message", "Comanda creada correctament");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error creant comanda: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Afegir producte a una comanda existent (només si està en estat PENDENT)
     * POST /api/comandes/{id}/productes
     */
    @PostMapping("/{id}/productes")
    public ResponseEntity<Map<String, Object>> afegirProducteAComanda(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> producteData) {
        try {
            Comanda comandaActualitzada = comandaService.afegirProducte(id, producteData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandaActualitzada);
            response.put("message", "Producte afegit a la comanda correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error afegint producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Eliminar producte d'una comanda (només si està en estat PENDENT)
     * DELETE /api/comandes/{id}/productes/{detallId}
     */
    @DeleteMapping("/{id}/productes/{detallId}")
    public ResponseEntity<Map<String, Object>> eliminarProducteDeComanda(
            @PathVariable Long id, 
            @PathVariable Long detallId) {
        try {
            Comanda comandaActualitzada = comandaService.eliminarProducte(id, detallId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandaActualitzada);
            response.put("message", "Producte eliminat de la comanda correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error eliminant producte: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualitzar l'estat d'una comanda
     * PUT /api/comandes/{id}/estat
     */
    @PutMapping("/{id}/estat")
    public ResponseEntity<Map<String, Object>> actualitzarEstatComanda(
            @PathVariable Long id, 
            @RequestBody Map<String, String> estatData) {
        try {
            String nouEstat = estatData.get("estat");
            Comanda comandaActualitzada = comandaService.actualitzarEstat(id, EstatComanda.valueOf(nouEstat));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandaActualitzada);
            response.put("message", "Estat de comanda actualitzat correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant estat: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Confirmar comanda (canviar de PENDENT a CONFIRMADA)
     * POST /api/comandes/{id}/confirmar
     */
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Map<String, Object>> confirmarComanda(@PathVariable Long id) {
        try {
            Comanda comandaConfirmada = comandaService.confirmarComanda(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandaConfirmada);
            response.put("message", "Comanda confirmada correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error confirmant comanda: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Cancel·lar comanda
     * POST /api/comandes/{id}/cancelar
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarComanda(@PathVariable Long id) {
        try {
            Comanda comandaCancelada = comandaService.cancelarComanda(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandaCancelada);
            response.put("message", "Comanda cancel·lada correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error cancel·lant comanda: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir estadístiques de comandes
     * GET /api/comandes/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getComandesStats(
            @RequestParam(required = false) Long usuari_id,
            @RequestParam(required = false) String periode) {
        try {
            Map<String, Object> stats = comandaService.getStats(usuari_id, periode);
            
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

    /**
     * Obtenir historial de canvis d'estat d'una comanda
     * GET /api/comandes/{id}/historial
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<Map<String, Object>> getHistorialComanda(@PathVariable Long id) {
        try {
            Map<String, Object> historial = comandaService.getHistorialCanvis(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", historial);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint historial: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}