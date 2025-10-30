package com.bicifood.controller;

import com.bicifood.model.Usuari;
import com.bicifood.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller per gestionar autenticació
 * Proporciona endpoints per login, logout i gestió de sessions
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Login d'usuari
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> loginData,
            HttpServletRequest request) {
        try {
            String email = loginData.get("email");
            String contrasenya = loginData.get("contrasenya");

            if (email == null || contrasenya == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Email i contrasenya són obligatoris");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            Usuari usuari = authService.authenticate(email, contrasenya);
            
            if (usuari != null) {
                // Crear sessió
                HttpSession session = request.getSession(true);
                session.setAttribute("usuari_id", usuari.getId());
                session.setAttribute("usuari_rol", usuari.getRol().name());
                session.setAttribute("usuari_nom", usuari.getNom());
                session.setMaxInactiveInterval(3600); // 1 hora

                // Actualitzar últim accés
                authService.updateUltimAcces(usuari.getId());

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login correcte");
                response.put("usuari", Map.of(
                    "id", usuari.getId(),
                    "nom", usuari.getNom(),
                    "cognom", usuari.getCognom(),
                    "email", usuari.getEmail(),
                    "rol", usuari.getRol(),
                    "actiu", usuari.getActiu()
                ));
                response.put("session_id", session.getId());

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Credencials incorrectes");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error durant el login: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Logout d'usuari
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                session.invalidate();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout correcte");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error durant el logout: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Registre de nou usuari
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody Usuari usuari) {
        try {
            Usuari nouUsuari = authService.register(usuari);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuari registrat correctament");
            response.put("usuari", Map.of(
                "id", nouUsuari.getId(),
                "nom", nouUsuari.getNom(),
                "cognom", nouUsuari.getCognom(),
                "email", nouUsuari.getEmail(),
                "rol", nouUsuari.getRol()
            ));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error durant el registre: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verificar sessió actual
     * GET /api/auth/session
     */
    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null && session.getAttribute("usuari_id") != null) {
                Long usuariId = (Long) session.getAttribute("usuari_id");
                Usuari usuari = authService.findById(usuariId);

                if (usuari != null && usuari.getActiu()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("authenticated", true);
                    response.put("usuari", Map.of(
                        "id", usuari.getId(),
                        "nom", usuari.getNom(),
                        "cognom", usuari.getCognom(),
                        "email", usuari.getEmail(),
                        "rol", usuari.getRol(),
                        "actiu", usuari.getActiu()
                    ));
                    response.put("session_info", Map.of(
                        "session_id", session.getId(),
                        "creation_time", session.getCreationTime(),
                        "last_accessed", session.getLastAccessedTime(),
                        "max_inactive_interval", session.getMaxInactiveInterval()
                    ));

                    return ResponseEntity.ok(response);
                } else {
                    // Usuari no existeix o no està actiu, invalidar sessió
                    session.invalidate();
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("authenticated", false);
            response.put("message", "No hi ha sessió activa");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error verificant sessió: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Canviar contrasenya (usuari autenticat)
     * POST /api/auth/change-password
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestBody Map<String, String> passwordData,
            HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session == null || session.getAttribute("usuari_id") == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Sessió no vàlida");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Long usuariId = (Long) session.getAttribute("usuari_id");
            String contrasenyaActual = passwordData.get("contrasenya_actual");
            String novaContrasenya = passwordData.get("nova_contrasenya");

            authService.canviarContrasenya(usuariId, contrasenyaActual, novaContrasenya);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contrasenya canviada correctament");

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
     * Renovar sessió
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshSession(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null && session.getAttribute("usuari_id") != null) {
                Long usuariId = (Long) session.getAttribute("usuari_id");
                Usuari usuari = authService.findById(usuariId);

                if (usuari != null && usuari.getActiu()) {
                    // Renovar sessió
                    session.setMaxInactiveInterval(3600); // 1 hora més

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Sessió renovada correctament");
                    response.put("expires_in", session.getMaxInactiveInterval());

                    return ResponseEntity.ok(response);
                } else {
                    session.invalidate();
                }
            }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessió no vàlida");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error renovant sessió: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verificar permisos d'usuari
     * GET /api/auth/permissions
     */
    @GetMapping("/permissions")
    public ResponseEntity<Map<String, Object>> checkPermissions(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null && session.getAttribute("usuari_id") != null) {
                String rol = (String) session.getAttribute("usuari_rol");
                
                Map<String, Boolean> permissions = authService.getUserPermissions(rol);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("rol", rol);
                response.put("permissions", permissions);

                return ResponseEntity.ok(response);
            }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessió no vàlida");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error verificant permisos: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtenir informació del perfil de l'usuari autenticat
     * GET /api/auth/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null && session.getAttribute("usuari_id") != null) {
                Long usuariId = (Long) session.getAttribute("usuari_id");
                Usuari usuari = authService.findById(usuariId);

                if (usuari != null && usuari.getActiu()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    Map<String, Object> usuariData = new HashMap<>();
                    usuariData.put("id", usuari.getId());
                    usuariData.put("nom", usuari.getNom());
                    usuariData.put("cognom", usuari.getCognom());
                    usuariData.put("email", usuari.getEmail());
                    usuariData.put("telefon", usuari.getTelefon() != null ? usuari.getTelefon() : "");
                    usuariData.put("adreca", usuari.getAdreca() != null ? usuari.getAdreca() : "");
                    usuariData.put("ciutat", usuari.getCiutat() != null ? usuari.getCiutat() : "");
                    usuariData.put("codi_postal", usuari.getCodiPostal() != null ? usuari.getCodiPostal() : "");
                    usuariData.put("rol", usuari.getRol());
                    usuariData.put("actiu", usuari.getActiu());
                    usuariData.put("verificat", usuari.getVerificat());
                    usuariData.put("data_registre", usuari.getDataRegistre());
                    usuariData.put("data_ultim_acces", usuari.getDataUltimAcces());
                    
                    response.put("usuari", usuariData);

                    return ResponseEntity.ok(response);
                }
            }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessió no vàlida");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint perfil: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}