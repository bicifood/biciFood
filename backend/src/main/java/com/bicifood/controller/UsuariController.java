package com.bicifood.controller;

import com.bicifood.model.Usuari;
import com.bicifood.model.Comanda;
import com.bicifood.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller per gestionar usuaris
 * Proporciona endpoints per operacions CRUD sobre usuaris i gestió de perfils
 */
@RestController
@RequestMapping("/api/usuaris")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class UsuariController {

    @Autowired
    private UsuariService usuariService;

    /**
     * Obtenir tots els usuaris (amb paginació)
     * GET /api/usuaris?page=0&size=10&rol=CLIENT
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsuaris(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String cerca) {
        try {
            Map<String, Object> result = usuariService.findAllWithPagination(page, size, rol, cerca);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result.get("usuaris"));
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
            errorResponse.put("error", "Error obtenint usuaris: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir un usuari específic per ID
     * GET /api/usuaris/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUsuariById(@PathVariable Long id) {
        try {
            Usuari usuari = usuariService.findById(id);
            
            if (usuari != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", usuari);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Usuari no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir usuari per email
     * GET /api/usuaris/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getUsuariByEmail(@PathVariable String email) {
        try {
            Usuari usuari = usuariService.findByEmail(email);
            
            if (usuari != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", usuari);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Usuari no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint de test per crear usuari simple
     * POST /api/usuaris/test
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testCreateUsuari(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Dades rebudes correctament");
        response.put("received", data);
        return ResponseEntity.ok(response);
    }

    /**
     * Crear usuari nou (versió simplificada)
     * POST /api/usuaris/create
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUsuariSimple(@RequestBody Map<String, Object> userData) {
        try {
            // Crear usuari manualment amb dades mínimes
            Usuari nouUsuari = new Usuari();
            nouUsuari.setNom((String) userData.get("nom"));
            nouUsuari.setCognom((String) userData.get("cognom"));
            nouUsuari.setEmail((String) userData.get("email"));
            
            // Contrasenya per defecte si no es proporciona
            String password = (String) userData.getOrDefault("contrasenya", "123456");
            nouUsuari.setContrasenya(password);
            
            // Crear usuari amb el service
            Usuari usuariCreat = usuariService.create(nouUsuari);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuariCreat);
            response.put("message", "Usuari creat correctament");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error creant usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear un nou usuari (registre)
     * POST /api/usuaris
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUsuari(@RequestBody Usuari usuari) {
        try {
            Usuari nouUsuari = usuariService.create(usuari);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", nouUsuari);
            response.put("message", "Usuari creat correctament");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error creant usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualitzar perfil d'usuari
     * PUT /api/usuaris/{id}
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> updateUsuari(@PathVariable Long id, @Valid @RequestBody Usuari usuari) {
        try {
            Usuari usuariExistent = usuariService.findById(id);
            
            if (usuariExistent != null) {
                usuari.setId(id);
                Usuari usuariActualitzat = usuariService.update(usuari);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", usuariActualitzat);
                response.put("message", "Usuari actualitzat correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Usuari no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Canviar contrasenya d'usuari
     * PUT /api/usuaris/{id}/contrasenya
     */
    @PutMapping("/{id}/contrasenya")
    public ResponseEntity<Map<String, Object>> canviarContrasenya(
            @PathVariable Long id, 
            @RequestBody Map<String, String> contrasenyaData) {
        try {
            String contrasenyaActual = contrasenyaData.get("contrasenya_actual");
            String novaContrasenya = contrasenyaData.get("nova_contrasenya");
            
            usuariService.canviarContrasenya(id, contrasenyaActual, novaContrasenya);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contrasenya actualitzada correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error canviant contrasenya: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Activar/Desactivar usuari
     * PUT /api/usuaris/{id}/estat
     */
    @PutMapping("/{id}/estat")
    public ResponseEntity<Map<String, Object>> canviarEstatUsuari(
            @PathVariable Long id, 
            @RequestBody Map<String, Boolean> estatData) {
        try {
            Boolean actiu = estatData.get("actiu");
            Usuari usuariActualitzat = usuariService.canviarEstat(id, actiu);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuariActualitzat);
            response.put("message", "Estat d'usuari actualitzat correctament");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error canviant estat: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir historial de comandes d'un usuari
     * GET /api/usuaris/{id}/comandes
     */
    @GetMapping("/{id}/comandes")
    public ResponseEntity<Map<String, Object>> getComandesUsuari(@PathVariable Long id) {
        try {
            List<Comanda> comandes = usuariService.getComandesUsuari(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comandes);
            response.put("count", comandes.size());
            response.put("usuari_id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint comandes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir estadístiques d'un usuari
     * GET /api/usuaris/{id}/stats
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStatsUsuari(@PathVariable Long id) {
        try {
            Map<String, Object> stats = usuariService.getUsuariStats(id);
            
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
     * Eliminar usuari (soft delete)
     * DELETE /api/usuaris/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUsuari(@PathVariable Long id) {
        try {
            Usuari usuari = usuariService.findById(id);
            
            if (usuari != null) {
                usuariService.delete(id);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Usuari eliminat correctament");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Usuari no trobat");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error eliminant usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verificar disponibilitat d'email
     * GET /api/usuaris/check-email?email=example@test.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailDisponible(@RequestParam String email) {
        try {
            boolean disponible = usuariService.isEmailDisponible(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("disponible", disponible);
            response.put("email", email);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error verificant email: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verificar disponibilitat de nom d'usuari
     * GET /api/usuaris/check-username?username=testuser
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsernameDisponible(@RequestParam String username) {
        try {
            boolean disponible = usuariService.isUsernameDisponible(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("disponible", disponible);
            response.put("username", username);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error verificant nom d'usuari: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir estadístiques generals d'usuaris (només admin)
     * GET /api/usuaris/stats/general
     */
    @GetMapping("/stats/general")
    public ResponseEntity<Map<String, Object>> getGeneralStats() {
        try {
            Map<String, Object> stats = usuariService.getGeneralStats();
            
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