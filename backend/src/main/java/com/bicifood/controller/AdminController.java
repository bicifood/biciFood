package com.bicifood.controller;

import com.bicifood.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller per funcions administratives
 * Proporciona endpoints per gestió del sistema i estadístiques generals
 * Només accessible per usuaris amb rol ADMIN
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Verificar permisos d'administrador abans de cada operació
     */
    private ResponseEntity<Map<String, Object>> checkAdminPermissions(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuari_id") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessió no vàlida");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        String rol = (String) session.getAttribute("usuari_rol");
        if (!"ADMIN".equals(rol)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Permisos insuficients. Només administradors.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        return null; // Permisos correctes
    }

    /**
     * Dashboard principal amb estadístiques generals
     * GET /api/admin/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> dashboardData = adminService.getDashboardData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", dashboardData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint dades del dashboard: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Estadístiques detallades del sistema
     * GET /api/admin/stats?periode=mes&tipus=vendes
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDetailedStats(
            @RequestParam(defaultValue = "mes") String periode,
            @RequestParam(defaultValue = "general") String tipus,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> stats = adminService.getDetailedStats(periode, tipus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("periode", periode);
            response.put("tipus", tipus);
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
     * Gestió d'usuaris - llistar tots amb filtres avançats
     * GET /api/admin/users?page=0&size=20&rol=CLIENT&actiu=true&ordenar=data_registre
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> manageUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean actiu,
            @RequestParam(defaultValue = "dataRegistre") String ordenar,
            @RequestParam(required = false) String cerca,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> usersData = adminService.getUsersWithAdvancedFilters(
                page, size, rol, actiu, ordenar, cerca);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usersData.get("usuaris"));
            response.put("pagination", Map.of(
                "current_page", page,
                "page_size", size,
                "total_elements", usersData.get("totalElements"),
                "total_pages", usersData.get("totalPages")
            ));
            response.put("filters_applied", Map.of(
                "rol", rol,
                "actiu", actiu,
                "ordenar", ordenar,
                "cerca", cerca
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error gestionant usuaris: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Activar/Desactivar usuari en massa
     * PUT /api/admin/users/bulk-status
     */
    @PutMapping("/users/bulk-status")
    public ResponseEntity<Map<String, Object>> bulkUpdateUserStatus(
            @RequestBody Map<String, Object> requestData,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> userIds = (java.util.List<Long>) requestData.get("user_ids");
            Boolean actiu = (Boolean) requestData.get("actiu");
            
            int updated = adminService.bulkUpdateUserStatus(userIds, actiu);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("%d usuaris actualitzats correctament", updated));
            response.put("users_updated", updated);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant usuaris: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Gestió de comandes - Vista administrativa
     * GET /api/admin/comandes?page=0&size=20&estat=PENDENT&usuari_id=1
     */
    @GetMapping("/comandes")
    public ResponseEntity<Map<String, Object>> manageOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String estat,
            @RequestParam(required = false) Long usuari_id,
            @RequestParam(required = false) String data_inici,
            @RequestParam(required = false) String data_fi,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> ordersData = adminService.getOrdersWithAdvancedFilters(
                page, size, estat, usuari_id, data_inici, data_fi);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", ordersData.get("comandes"));
            response.put("pagination", Map.of(
                "current_page", page,
                "page_size", size,
                "total_elements", ordersData.get("totalElements"),
                "total_pages", ordersData.get("totalPages")
            ));
            response.put("filters_applied", Map.of(
                "estat", estat,
                "usuari_id", usuari_id,
                "data_inici", data_inici,
                "data_fi", data_fi
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error gestionant comandes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Gestió de productes amb alertes d'stock
     * GET /api/admin/products?page=0&size=20&stock_baix=true&categoria_id=1
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> manageProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean stock_baix,
            @RequestParam(required = false) Long categoria_id,
            @RequestParam(required = false) Boolean actiu,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> productsData = adminService.getProductsWithAdvancedFilters(
                page, size, stock_baix, categoria_id, actiu);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productsData.get("productes"));
            response.put("pagination", Map.of(
                "current_page", page,
                "page_size", size,
                "total_elements", productsData.get("totalElements"),
                "total_pages", productsData.get("totalPages")
            ));
            response.put("alerts", productsData.get("alerts"));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error gestionant productes: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Reports i exportació de dades
     * GET /api/admin/reports?tipus=vendes&format=json&periode=mes
     */
    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> generateReports(
            @RequestParam String tipus,
            @RequestParam(defaultValue = "json") String format,
            @RequestParam(defaultValue = "mes") String periode,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> reportData = adminService.generateReport(tipus, format, periode);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("report_type", tipus);
            response.put("format", format);
            response.put("periode", periode);
            response.put("data", reportData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error generant report: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Configuració del sistema
     * GET /api/admin/system-config
     */
    @GetMapping("/system-config")
    public ResponseEntity<Map<String, Object>> getSystemConfig(HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> config = adminService.getSystemConfiguration();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", config);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint configuració: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualitzar configuració del sistema
     * PUT /api/admin/system-config
     */
    @PutMapping("/system-config")
    public ResponseEntity<Map<String, Object>> updateSystemConfig(
            @RequestBody Map<String, Object> configData,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> updatedConfig = adminService.updateSystemConfiguration(configData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Configuració actualitzada correctament");
            response.put("data", updatedConfig);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error actualitzant configuració: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Logs del sistema
     * GET /api/admin/logs?page=0&size=50&nivell=ERROR
     */
    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> getSystemLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String nivell,
            @RequestParam(required = false) String data_inici,
            @RequestParam(required = false) String data_fi,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            Map<String, Object> logsData = adminService.getSystemLogs(page, size, nivell, data_inici, data_fi);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", logsData.get("logs"));
            response.put("pagination", Map.of(
                "current_page", page,
                "page_size", size,
                "total_elements", logsData.get("totalElements"),
                "total_pages", logsData.get("totalPages")
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error obtenint logs: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Neteja i manteniment del sistema
     * POST /api/admin/maintenance
     */
    @PostMapping("/maintenance")
    public ResponseEntity<Map<String, Object>> performMaintenance(
            @RequestBody Map<String, Object> maintenanceData,
            HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> permissionCheck = checkAdminPermissions(request);
        if (permissionCheck != null) return permissionCheck;

        try {
            String operation = (String) maintenanceData.get("operation");
            Map<String, Object> result = adminService.performMaintenance(operation, maintenanceData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Operació de manteniment completada");
            response.put("operation", operation);
            response.put("result", result);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error durant el manteniment: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}